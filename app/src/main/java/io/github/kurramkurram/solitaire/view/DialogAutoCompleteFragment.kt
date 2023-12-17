package io.github.kurramkurram.solitaire.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import io.github.kurramkurram.solitaire.databinding.DialogAutoCompleteBinding
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_AUTO_COMPLETE
import io.github.kurramkurram.solitaire.viewmodel.SolitaireViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_auto_complete.*

class DialogAutoCompleteFragment : DialogBaseFragment() {

    private val solitaireViewModel: SolitaireViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAutoCompleteBinding.inflate(layoutInflater, container, false).apply {
            this.viewModel = solitaireViewModel
        }

        return Dialog(requireContext()).apply {
            setContentView(binding.root)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            positive_button.setOnClickListener { onPositiveClick(DIALOG_RESULT_AUTO_COMPLETE) }
            negative_button.setOnClickListener { onNegativeClick(DIALOG_RESULT_AUTO_COMPLETE) }
        }
    }
}

