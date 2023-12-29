package io.github.kurramkurram.solitaire.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_RESET
import kotlinx.android.synthetic.main.dialog_reset.*

/**
 * リセットダイアログ.
 */
class DialogResetFragment : DialogBaseFragment() {

    companion object {
        private const val KEY_DIALOG_TITLE = "key_dialog_title"
        private const val KEY_DIALOG_TEXT = "key_dialog_text"

        fun newInstance(title: String, text: String): DialogResetFragment {
            val args = Bundle()
            args.putString(KEY_DIALOG_TITLE, title)
            args.putString(KEY_DIALOG_TEXT, text)
            val fragment = DialogResetFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_reset)
            requireArguments().getString(KEY_DIALOG_TITLE)?.let { title.text = it }
            requireArguments().getString(KEY_DIALOG_TEXT)?.let { text.text = it }
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            positive_button.setOnClickListener { onPositiveClick(DIALOG_RESULT_RESET) }
            negative_button.setOnClickListener { onNegativeClick(DIALOG_RESULT_RESET) }
        }
}