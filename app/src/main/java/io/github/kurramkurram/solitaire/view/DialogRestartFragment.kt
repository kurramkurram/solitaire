package io.github.kurramkurram.solitaire.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import io.github.kurramkurram.solitaire.databinding.DialogRestartBinding
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_RESTART

/**
 * 新規ゲームダイアログ.
 */
class DialogRestartFragment : DialogBaseFragment() {

    private lateinit var binding: DialogRestartBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogRestartBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(activity).apply {
            setView(binding.root)
            binding.positiveButton.setOnClickListener { onPositiveClick(DIALOG_RESULT_RESTART) }
            binding.negativeButton.setOnClickListener { onNegativeClick(DIALOG_RESULT_RESTART) }
        }
        return builder.create().apply {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}
