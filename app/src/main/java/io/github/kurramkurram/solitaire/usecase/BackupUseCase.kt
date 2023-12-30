package io.github.kurramkurram.solitaire.usecase

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import io.github.kurramkurram.solitaire.database.RecordDatabase
import io.github.kurramkurram.solitaire.repository.ArchiveRepository
import io.github.kurramkurram.solitaire.repository.ArchiveRepositoryImpl

/**
 * 保存ユースケース.
 *
 * @param context [Context]
 * @param user Firebaseユーザ
 * @param archiveRepository データの保存・復元リポジトリ
 * @param onSuccess 成功時の処理
 * @param onFailure 失敗時の処理
 */
class BackupUseCase(
    private val context: Context,
    private val user: FirebaseUser,
    private val archiveRepository: ArchiveRepository = ArchiveRepositoryImpl(context),
    private val onSuccess: () -> Unit,
    private val onFailure: () -> Unit
) {

    fun invoke() {
        val storage = FirebaseStorage.getInstance()
        val ref = storage.reference.child("users/${user.uid}/backup.zip")
        val dbFile = archiveRepository.getDatabaseFile(RecordDatabase.DB_NAME)
        val zipFile = archiveRepository.getCompressedFile("backup.zip")
        val file = archiveRepository.compressFile(dbFile, zipFile)
        ref.putFile(Uri.fromFile(file))
            .addOnSuccessListener {
                onSuccess()
                archiveRepository.deleteFile(zipFile)
            }
            .addOnFailureListener {
                onFailure()
                archiveRepository.deleteFile(zipFile)
            }
    }
}
