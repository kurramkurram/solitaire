package io.github.kurramkurram.solitaire.view

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.kurramkurram.solitaire.R
import kotlinx.android.synthetic.main.dialog_play_movie.*

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        Dialog(requireContext(), R.style.Theme_TranslucentBackground).apply {
            setContentView(R.layout.dialog_play_movie)
            close.setOnClickListener { dismiss() }
            video_view.apply {
                val uri = requireArguments().getString(KEY_FILE_URI)
                setVideoURI(Uri.parse(uri))
                setOnPreparedListener {
                    it?.isLooping = true
                    start()
                }
            }
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
