package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.kurramkurram.solitaire.BuildConfig
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.util.L
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.app_version_name_tv.text = BuildConfig.VERSION_NAME

        view.open_source_software_container.setOnClickListener {
            L.d(TAG, "#SettingsFragment open_source_software_container")
        }

        view.application_privacy_policy_container.setOnClickListener {
            L.d(TAG, "#SettingsFragment application_privacy_policy_container")
        }

        view.question.setOnClickListener {
            L.d(TAG, "#SettingsFragment question")
        }
    }

    companion object {
        const val TAG = "SettingsFragment"
    }
}