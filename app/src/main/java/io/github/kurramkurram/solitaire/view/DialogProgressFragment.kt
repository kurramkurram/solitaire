package io.github.kurramkurram.solitaire.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.kurramkurram.solitaire.R
import kotlinx.android.synthetic.main.dialog_progress.*

class DialogProgressFragment : DialogBaseFragment() {

    companion object {
        private const val KEY_MESSAGE = "key_message"

        fun newInstance(message: String): DialogProgressFragment {
            val args = Bundle()
            args.putString(KEY_MESSAGE, message)
            val fragment = DialogProgressFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        Dialog(requireContext()).apply {
            val message = requireArguments().getString(KEY_MESSAGE)
            setContentView(R.layout.dialog_progress)
            progress_message.text = message

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