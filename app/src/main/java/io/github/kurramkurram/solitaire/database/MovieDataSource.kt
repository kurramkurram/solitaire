package io.github.kurramkurram.solitaire.database

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.name

abstract class MovieDataSource {
    abstract fun getAllFile(): List<File>

    abstract fun getSaveFile(): File

    abstract fun deleteOldestFile(): Boolean
}

class MovieDataSourceImpl(private val context: Context) : MovieDataSource() {

    override fun getAllFile(): List<File> {
        val list = mutableListOf<File>()
        Files.list(Paths.get(context.getExternalFilesDir(null)!!.path)).use {
            for (f in it) {
                list.add(f.toFile())
                Log.d(TAG, "#getAllFile = ${f.name}")
            }
        }
        return list.sortedByDescending { it.name }.filterIndexed { index, _ -> index < 6 }
    }

    @SuppressLint("SimpleDateFormat")
    override fun getSaveFile(): File {
        val filePath =
            "${context.getExternalFilesDir(null)!!.path}/${System.currentTimeMillis()}.mp4"
        return File(filePath)
    }

    override fun deleteOldestFile(): Boolean {
        Files.list(Paths.get(context.getExternalFilesDir(null)!!.path)).use {
            if (it.count() < 7) return true
        }
        Files.list(Paths.get(context.getExternalFilesDir(null)!!.path)).use {
            val file = it.findFirst().get().toFile()
            Log.d(TAG, "#deleteOldestFIle = ${file.name}")
            return file.delete()
        }
    }

    companion object {
        private const val TAG = "MovieDataSourceImpl"
    }
}