package io.github.kurramkurram.solitaire.view

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import io.github.kurramkurram.solitaire.BuildConfig
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.util.DATE_PATTERN
import io.github.kurramkurram.solitaire.util.L
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import java.text.SimpleDateFormat
import java.util.*

class SettingsFragment : Fragment(), View.OnClickListener {

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