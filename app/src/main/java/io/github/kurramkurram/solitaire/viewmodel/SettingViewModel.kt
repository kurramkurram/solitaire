package io.github.kurramkurram.solitaire.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.github.kurramkurram.solitaire.usecase.BackupUseCase
import io.github.kurramkurram.solitaire.usecase.FirebaseAuthenticationUseCase
import io.github.kurramkurram.solitaire.usecase.RestoreUseCase

class SettingViewModel(private val application: Application) : AndroidViewModel(application) {

    /**
     * データのバックアップを開始.
     *
     * @param intent [Intent]
     * @param onSuccess 成功時の処理
     * @param onFailure 失敗時の処理
     */
    fun startBackUpData(
        intent: Intent,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        fun backUpUseCase(user: FirebaseUser) = BackupUseCase(
            application.applicationContext,
            user,
            onSuccess = { onSuccess() },
            onFailure = { onFailure() }
        ).invoke()

        FirebaseAuthenticationUseCase(
            intent,
            onSuccess = { FirebaseAuth.getInstance().currentUser?.let { user -> backUpUseCase(user) } },
            onFailure = { onFailure() }
        ).invoke()
    }

    /**
     * 復元を開始する.
     *
     * @param intent [Intent]
     * @param onSuccess 成功時の処理
     * @param onFailure 失敗時の処理
     */
    fun startRestoreData(
        intent: Intent,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        fun restoreUseCase(user: FirebaseUser) = RestoreUseCase(
            application.applicationContext,
            user,
            onSuccess = { onSuccess() },
            onFailure = { onFailure() }
        ).invoke()

        FirebaseAuthenticationUseCase(
            intent,
            onSuccess = { FirebaseAuth.getInstance().currentUser?.let { user -> restoreUseCase(user) } },
            onFailure = { onFailure() }
        ).invoke()
    }
}
