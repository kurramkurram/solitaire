package io.github.kurramkurram.solitaire.data

import android.content.Context
import io.github.kurramkurram.solitaire.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * ライブラリのライセンスリスト.
 *
 * @param licenseList ライセンスのリスト
 */
data class Licenses(val licenseList: List<LibraryLicenseDto>) :
    List<LibraryLicenseDto> by licenseList {
    companion object {
        /**
         * ライブラリのライセンスリストを生成する.
         *
         * @param context [Context]
         * @return ライブラリのライセンスリスト
         */
        suspend fun create(context: Context): Licenses {
            val licenses =
                loadLibraries(context).map {
                    LibraryLicenseDto(it.name, loadLicense(context, it))
                }
            return Licenses(licenses)
        }

        /**
         * ライセンスのmetaデータを取得する.
         *
         * @param context [Context]
         * @return ライセンスのmetaデータ
         */
        private suspend fun loadLibraries(context: Context): List<LibraryMetaDto> {
            return withContext(Dispatchers.IO) {
                val inputSteam =
                    context.resources.openRawResource(R.raw.third_party_license_metadata)
                inputSteam.use { input ->
                    val reader = BufferedReader(InputStreamReader(input, "UTF-8"))
                    reader.use { bufferedReader ->
                        val libraries = mutableListOf<LibraryMetaDto>()
                        while (true) {
                            val line = bufferedReader.readLine() ?: break
                            val (position, name) = line.split(' ', limit = 2)
                            val (offset, length) = position.split(':').map { it.toInt() }
                            libraries.add(LibraryMetaDto(name, offset, length))
                        }
                        libraries.toList()
                    }
                }
            }
        }

        /**
         * ライセンスを取得する.
         *
         * @param context [Context]
         * @param library ライブラリのメタデータ
         */
        private suspend fun loadLicense(
            context: Context,
            library: LibraryMetaDto,
        ): String {
            return withContext(Dispatchers.IO) {
                val charArray = CharArray(library.length)
                val inputStream = context.resources.openRawResource(R.raw.third_party_licenses)
                inputStream.use { stream ->
                    val bufferedReader = BufferedReader(InputStreamReader(stream, "UTF-8"))
                    bufferedReader.use { reader ->
                        reader.skip(library.offset.toLong())
                        reader.read(charArray, 0, library.length)
                    }
                }
                String(charArray)
            }
        }
    }
}

/**
 * ライブラリのライセンス.
 *
 * @param name ライセンス名
 * @param terms 条文
 */
data class LibraryLicenseDto(
    val name: String,
    val terms: String,
)

/**
 * ライブラリのメタデータ.
 *
 * @param name ライブラリ名
 * @param offset third_party_licensesのオフセット
 * @param length third_party_licensesの長さ
 */
data class LibraryMetaDto(
    val name: String,
    val offset: Int,
    val length: Int,
)