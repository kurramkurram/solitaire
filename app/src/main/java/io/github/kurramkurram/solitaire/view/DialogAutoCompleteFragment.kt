package io.github.kurramkurram.solitaire.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import io.github.kurramkurram.solitaire.databinding.DialogAutoCompleteBinding
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_AUTO_COMPLETE
import io.github.kurramkurram.solitaire.viewmodel.SolitaireViewModel

/**
 * 自動回収ダイアログ.
 */
class DialogAutoCompleteFragment : DialogBaseFragment() {

    private val solitaireViewModel: SolitaireViewModel by activityViewModels()
    private lateinit var binding: DialogAutoCompleteBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAutoCompleteBinding.inflate(layoutInflater).apply {
            this.viewModel = solitaireViewModel
        }
        val builder = AlertDialog.Builder(requireContext()).apply {
            setView(binding.root)
            binding.positiveButton.setOnClickListener { onPositiveClick(DIALOG_RESULT_AUTO_COMPLETE) }
            binding.negativeButton.setOnClickListener { onNegativeClick(DIALOG_RESULT_AUTO_COMPLETE) }
        }
        return builder.create().apply {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}
