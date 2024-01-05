package io.github.kurramkurram.solitaire.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import io.github.kurramkurram.solitaire.databinding.DialogStartMovieBinding
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_START_MOVIE
import io.github.kurramkurram.solitaire.viewmodel.SolitaireViewModel

class DialogStartMovieFragment : DialogBaseFragment() {

    private val solitaireViewModel: SolitaireViewModel by activityViewModels()
    private lateinit var binding: DialogStartMovieBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogStartMovieBinding.inflate(layoutInflater).apply {
            this.viewModel = solitaireViewModel
        }
        val builder = AlertDialog.Builder(activity).apply {
            setView(binding.root)
            binding.positiveButton.setOnClickListener { onPositiveClick(DIALOG_RESULT_START_MOVIE) }
            binding.negativeButton.setOnClickListener { onNegativeClick(DIALOG_RESULT_START_MOVIE) }
        }
        return builder.create().apply {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}
