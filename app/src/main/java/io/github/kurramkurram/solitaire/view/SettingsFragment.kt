package io.github.kurramkurram.solitaire.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.play.core.review.ReviewManagerFactory
import io.github.kurramkurram.solitaire.BuildConfig
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.databinding.FragmentSettingsBinding
import io.github.kurramkurram.solitaire.util.DATE_PATTERN
import io.github.kurramkurram.solitaire.util.L
import io.github.kurramkurram.solitaire.util.SHOW_DIALOG_KEY
import io.github.kurramkurram.solitaire.viewmodel.MainViewModel
import io.github.kurramkurram.solitaire.viewmodel.SettingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 設定画面.
 */
class SettingsFragment : Fragment(), View.OnClickListener {

    private lateinit var googleSignInClient: GoogleSignInClient
    private val settingViewModel: SettingViewModel by activityViewModels()
    private val mainViewModel by lazy {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    private lateinit var binding: FragmentSettingsBinding

    private val startBackupSignInForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val ctx = requireContext()
                val message = ctx.resources.getString(R.string.loading_dialog_saving)
                val progress = DialogProgressFragment.newInstance(message)
                progress.show(requireActivity().supportFragmentManager, SHOW_DIALOG_KEY)

                result.data?.let {
                    fun onSuccess() = CoroutineScope(Dispatchers.Main).launch {
                        progress.dismiss()
                        ctx.showToast(R.string.toast_backup_success)
                    }

                    fun onFailure() = CoroutineScope(Dispatchers.Main).launch {
                        progress.dismiss()
                        ctx.showToast(R.string.toast_backup_failure)
                    }

                    settingViewModel.startBackUpData(it, { onSuccess() }, { onFailure() })
                }
            }
        }

    private val startRestoreSignInForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val ctx = requireContext()
                val message = ctx.resources.getString(R.string.loading_dialog_restoring)
                val progress = DialogProgressFragment.newInstance(message)
                progress.show(requireActivity().supportFragmentManager, SHOW_DIALOG_KEY)

                result.data?.let {
                    fun onSuccess() = CoroutineScope(Dispatchers.Main).launch {
                        progress.dismiss()
                        ctx.showToast(R.string.toast_restore_success)
                    }

                    fun onFailure() = CoroutineScope(Dispatchers.Main).launch {
                        progress.dismiss()
                        ctx.showToast(R.string.toast_restore_failure)
                    }

                    settingViewModel.startRestoreData(it, { onSuccess() }, { onFailure() })
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false).apply {
            this.viewModel = mainViewModel
            this.lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            openSourceSoftware.setOnClickListener(this@SettingsFragment)
            applicationPrivacyPolicy.setOnClickListener(this@SettingsFragment)
            question.setOnClickListener(this@SettingsFragment)
            appShare.setOnClickListener(this@SettingsFragment)
            appAssessment.setOnClickListener(this@SettingsFragment)
            otherApp.setOnClickListener(this@SettingsFragment)
            appMusic.binding.switchButton.setOnClickListener(this@SettingsFragment)
            backup.setOnClickListener(this@SettingsFragment)
            restore.setOnClickListener(this@SettingsFragment)
            signOut.setOnClickListener(this@SettingsFragment)
            deleteAd.setOnClickListener(this@SettingsFragment)
            notification.setOnClickListener(this@SettingsFragment)
        }
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.application_privacy_policy -> {
                Intent(requireContext(), AppActivity::class.java).apply {
                    startActivity(this)
                }
            }

            R.id.open_source_software -> {
                Intent(requireContext(), OssLicensesMenuActivity::class.java).apply {
                    putExtra(EXTRA_TITLE, resources.getString(R.string.setting_oss))
                    startActivity(this)
                }
            }

            R.id.app_share -> {
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.setting_share_text))
                    try {
                        startActivity(Intent.createChooser(this, null))
                    } catch (e: Exception) {
                        L.e(TAG, "#onClick $e")
                    }
                }
            }

            R.id.app_assessment -> {
                val manager = ReviewManagerFactory.create(requireContext())
                val request = manager.requestReviewFlow()
                request.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val info = it.result
                        manager.launchReviewFlow(requireActivity(), info).apply {
                            addOnCompleteListener { Log.d(TAG, "#onClick onComplete") }
                            addOnSuccessListener { Log.d(TAG, "#onClick onSuccess") }
                            addOnCanceledListener { Log.d(TAG, "#onClick onCancel") }
                            addOnFailureListener { Log.d(TAG, "#onClick onFailure") }
                        }
                    } else {
                        Log.d(TAG, "#onClick app_assessment task = $it")
                    }
                }
            }

            R.id.question -> {
                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("kurram.dev@gmail.com"))
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

            R.id.other_app -> {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://play.google.com/store/apps/developer?id=Kurram")
                    try {
                        startActivity(this)
                    } catch (e: ActivityNotFoundException) {
                        L.e(TAG, "#onClick $e")
                    }
                }
            }

            R.id.delete_ad ->
                startActivity(Intent(requireContext(), NavigateProActivity::class.java))

            R.id.backup -> startBackupSignInForResult.launch(googleSignInClient.signInIntent)

            R.id.restore -> startRestoreSignInForResult.launch(googleSignInClient.signInIntent)

            R.id.switch_button -> {
                if (v is SwitchCompat && v == binding.appMusic.binding.switchButton) {
                    mainViewModel.onMusicCheckChanged(v.isChecked)
                }
            }

            R.id.notification ->
                startActivity(Intent(requireContext(), NotificationActivity::class.java))

            R.id.sign_out -> {
                googleSignInClient.signOut()
                requireContext().showToast(R.string.toast_sign_out)
            }
        }
    }

    /**
     * メールの本文にデフォルトで表示する文言.
     *
     * @return メールの本文
     */
    @SuppressLint("SimpleDateFormat")
    private fun createContactText(): String = resources.getString(
        R.string.setting_question_text,
        BuildConfig.VERSION_NAME,
        Build.VERSION.SDK_INT,
        Build.MODEL,
        SimpleDateFormat(DATE_PATTERN).format(Date())
    )

    companion object {
        const val TAG = "SettingsFragment"

        private const val EXTRA_TITLE = "title"
    }
}
