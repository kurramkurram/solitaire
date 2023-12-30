package io.github.kurramkurram.solitaire.usecase

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import io.github.kurramkurram.solitaire.data.Record
import io.github.kurramkurram.solitaire.database.TmpRecordDatabase
import io.github.kurramkurram.solitaire.repository.ArchiveRepository
import io.github.kurramkurram.solitaire.repository.ArchiveRepositoryImpl
import io.github.kurramkurram.solitaire.repository.RecordRepository
import io.github.kurramkurram.solitaire.repository.RecordRepositoryImpl
import io.github.kurramkurram.solitaire.repository.TmpRecordRepository
import io.github.kurramkurram.solitaire.repository.TmpRecordRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 復元ユースケース.
 *
 * @param context [Context]
 * @param user Firebaseユーザ
 * @param archiveRepository データの保存・復元リポジトリ
 * @param recordRepository 記録のリポジトリ
 * @param tmpRecordRepository 記録の復元リポジトリ
 * @param onSuccess 成功時の処理
 * @param onFailure 失敗時の処理
 */
class RestoreUseCase(
    private val context: Context,
    private val user: FirebaseUser,
    private val archiveRepository: ArchiveRepository = ArchiveRepositoryImpl(context),
    private val recordRepository: RecordRepository = RecordRepositoryImpl(context),
    private val tmpRecordRepository: TmpRecordRepository = TmpRecordRepositoryImpl(context),
    private val onSuccess: () -> Unit,
    private val onFailure: () -> Unit
) {

    fun invoke() {
        val tmpFile = archiveRepository.getCompressedFile("backup.zip")
        val storage = FirebaseStorage.getInstance()
        val ref = storage.reference.child("users/${user.uid}/backup.zip")
        ref.getFile(tmpFile)
            .addOnCompleteListener {
                archiveRepository.apply {
                    val zipFile = getCompressedFile("backup.zip")
                    val dbFile = getDatabaseFile(TmpRecordDatabase.DB_NAME)
                    val decompress =
                        decompressFile(zipFile, dbFile.parent!!, TmpRecordDatabase.DB_NAME)

                    CoroutineScope(Dispatchers.IO).launch {
                        val records: List<Record> = tmpRecordRepository.selectAll().map { record ->
                            record.id = 0
                            record
                        }

                        recordRepository.apply {
                            deleteAll()
                            saveRecord(records)
                        }
                        deleteFile(tmpFile)
                        deleteFile(dbFile)
                        if (decompress) {
                            onSuccess()
                        } else {
                            onFailure()
                        }
                    }
                }
            }
            .addOnFailureListener {
                onFailure()
                val zipFile = archiveRepository.getCompressedFile("backup.zip")
                archiveRepository.deleteFile(zipFile)
            }
    }
}
