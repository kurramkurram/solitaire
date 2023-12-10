package io.github.kurramkurram.solitaire.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.firebase.auth.FirebaseAuth
import io.github.kurramkurram.solitaire.BuildConfig
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.database.TmpRecordDatabase
import io.github.kurramkurram.solitaire.repository.ArchiveRepositoryImpl
import io.github.kurramkurram.solitaire.usecase.BackupUseCase
import io.github.kurramkurram.solitaire.usecase.FirebaseAuthenticationUseCase
import io.github.kurramkurram.solitaire.usecase.RestoreUseCase
import io.github.kurramkurram.solitaire.util.DATE_PATTERN
import io.github.kurramkurram.solitaire.util.L
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import java.text.SimpleDateFormat
import java.util.*

class SettingsFragment : Fragment(), View.OnClickListener {

    private lateinit var googleSignInClient: GoogleSignInClient

    private val startBackupSignInForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    val ctx = requireContext()
                    FirebaseAuthenticationUseCase(
                        it,
                        onSuccess = {
                            FirebaseAuth.getInstance().currentUser?.let { user ->
                                BackupUseCase(
                                    ctx,
                                    user,
                                    onSuccess = { ctx.showToast(R.string.toast_backup_success) },
                                    onFailure = { ctx.showToast(R.string.toast_backup_failure) }
                                ).invoke()
                            }
                        },
                        onFailure = { ctx.showToast(R.string.toast_backup_failure) }
                    ).invoke()
                }
            }
        }

    private val startRestoreSignInForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    val ctx = requireContext()
                    FirebaseAuthenticationUseCase(
                        it,
                        onSuccess = {
                            FirebaseAuth.getInstance().currentUser?.let { user ->
                                RestoreUseCase(
                                    ctx,
                                    user,
                                    onSuccess = { ctx.showToast(R.string.toast_restore_success) },
                                    onFailure = { ctx.showToast(R.string.toast_restore_failure) }
                                ).invoke()
                            }
                        },
                        onFailure = { ctx.showToast(R.string.toast_restore_failure) }
                    ).invoke()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.app_version_name_tv.text = BuildConfig.VERSION_NAME

        view.open_source_software_container.setOnClickListener(this)
        view.application_privacy_policy_container.setOnClickListener(this)
        view.question.setOnClickListener(this)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)

        backup.setOnClickListener(this)
        restore.setOnClickListener(this)
        sign_out.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            open_source_software_container -> {
                Intent(requireContext(), OssLicensesMenuActivity::class.java).apply {
                    putExtra(EXTRA_TITLE, resources.getString(R.string.setting_oss))
                    startActivity(this)
                }
            }

            application_privacy_policy_container -> {
                Intent(requireContext(), AppActivity::class.java).apply {
                    startActivity(this)
                }
            }

            question -> {
                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(
                        Intent.EXTRA_EMAIL,
                        arrayOf("kurram.dev@gmail.com")
                    )
                    val subject = requireContext().resources
                        .getString(R.string.setting_question_mail_subject)
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    val text = createContactText()
                    putExtra(Intent.EXTRA_TEXT, text)

                    try {
                        startActivity(this)
                    } catch (e: ActivityNotFoundException) {
                        L.e(TAG, "#onClick $e")
                    }
                }
            }

            backup -> {
                val signInIntent = googleSignInClient.signInIntent
                startBackupSignInForResult.launch(signInIntent)
            }

            restore -> {
                val signInIntent = googleSignInClient.signInIntent
                startRestoreSignInForResult.launch(signInIntent)
            }

            sign_out -> {
                googleSignInClient.signOut()
            }
        }
    }

    /**
     * メールの本文にデフォルトで表示する文言.
     *
     * @return メールの本文
     */
    @SuppressLint("SimpleDateFormat")
    private fun createContactText(): String =
        "アプリバージョン：${BuildConfig.VERSION_NAME}\n " +
                "OSバージョン：Android ${Build.VERSION.SDK_INT} \n " +
                "機種名：${Build.MODEL}\n " +
                "日にち：${SimpleDateFormat(DATE_PATTERN).format(Date())}"

    companion object {
        const val TAG = "SettingsFragment"

        private const val EXTRA_TITLE = "title"
    }
}