package io.github.kurramkurram.solitaire.usecase

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import io.github.kurramkurram.solitaire.database.RecordDatabase
import io.github.kurramkurram.solitaire.database.TmpRecordDatabase
import io.github.kurramkurram.solitaire.repository.*
import io.github.kurramkurram.solitaire.util.L
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import io.github.kurramkurram.solitaire.data.Record

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
                    val dbFile = getDatabaseFile(RecordDatabase.DB_NAME)
                    val decompress =
                        decompressFile(zipFile, dbFile.parent!!, TmpRecordDatabase.DB_NAME)
                    L.d("RestoreUsecase", "tmpFile = ${tmpFile.path}")

//                    CoroutineScope(Dispatchers.IO).launch {
////                        L.d("RestoreUseCase", "#invoke --1--")
////                        val records: List<Record> = tmpRecordRepository.selectAll().map { record ->
////                            record.id = 0
////                            L.d("RestoreUseCase", "#invoke data = $record")
////                            record
////                        }
//
////                        recordRepository.saveRecord(records)
//                        deleteFile(tmpFile)
//                        deleteFile(dbFile)
//                        CoroutineScope(Dispatchers.Main).launch {
//                            if (decompress) {
//                                onSuccess()
//                            } else {
//                                onFailure()
//                            }
//                        }
//                    }
                    if (decompress) {
                        onSuccess()
                    } else {
                        onFailure()
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