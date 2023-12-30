package io.github.kurramkurram.solitaire.usecase

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.github.kurramkurram.solitaire.util.L

/**
 * Firebase認証ユースケース.
 *
 * @param intent [Intent]
 * @param onSuccess 成功の処理
 * @param onFailure 失敗の処理
 */
class FirebaseAuthenticationUseCase(
    private val intent: Intent,
    private val onSuccess: () -> Unit,
    private val onFailure: () -> Unit
) {

    fun invoke() {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onSuccess()
                    } else {
                        onFailure()
                    }
                }
        } catch (e: Exception) {
            onFailure()
            L.e(TAG, "invoke $e")
        }
    }

    companion object {
        private const val TAG = "FirebaseAuthenticationUseCase"
    }
}
