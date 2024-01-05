package io.github.kurramkurram.solitaire.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import io.github.kurramkurram.solitaire.databinding.DialogResetBinding
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_RESET

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

    private lateinit var binding: DialogResetBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogResetBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(activity).apply {
            setView(binding.root)
            requireArguments().getString(KEY_DIALOG_TITLE)?.let { binding.title.text = it }
            requireArguments().getString(KEY_DIALOG_TEXT)?.let { binding.text.text = it }
            binding.positiveButton.setOnClickListener { onPositiveClick(DIALOG_RESULT_RESET) }
            binding.negativeButton.setOnClickListener { onNegativeClick(DIALOG_RESULT_RESET) }
        }
        return builder.create().apply {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}
