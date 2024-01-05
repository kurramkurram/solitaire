package io.github.kurramkurram.solitaire.view

import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.databinding.DialogPlayMovieBinding

/**
 * 動画再生画面ダイアログ.
 */
class DialogPlayMovieFragment : DialogBaseFragment() {

    companion object {
        private const val KEY_FILE_URI = "key_file_uri"

        fun newInstance(uri: String): DialogPlayMovieFragment {
            val args = Bundle()
            args.putString(KEY_FILE_URI, uri)
            val fragment = DialogPlayMovieFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: DialogPlayMovieBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPlayMovieBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(activity, R.style.Theme_TranslucentBackground).apply {
            setView(binding.root)
            binding.close.setOnClickListener { dismiss() }
            binding.videoView.apply {
                val uri = requireArguments().getString(KEY_FILE_URI)
                setVideoURI(Uri.parse(uri))
                setOnPreparedListener {
                    it?.isLooping = true
                    start()
                }
            }
        }
        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
