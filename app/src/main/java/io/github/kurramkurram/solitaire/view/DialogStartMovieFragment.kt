package io.github.kurramkurram.solitaire.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import io.github.kurramkurram.solitaire.databinding.DialogStartMovieBinding
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_START_MOVIE
import io.github.kurramkurram.solitaire.viewmodel.SolitaireViewModel
import kotlinx.android.synthetic.main.activity_main.container
import kotlinx.android.synthetic.main.dialog_start_movie.negative_button
import kotlinx.android.synthetic.main.dialog_start_movie.positive_button

class DialogStartMovieFragment : DialogBaseFragment() {

    private val solitaireViewModel: SolitaireViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogStartMovieBinding.inflate(layoutInflater, container, false).apply {
            this.viewModel = solitaireViewModel
        }
        return Dialog(requireContext()).apply {
            setContentView(binding.root)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            positive_button.setOnClickListener { onPositiveClick(DIALOG_RESULT_START_MOVIE) }
            negative_button.setOnClickListener { onNegativeClick(DIALOG_RESULT_START_MOVIE) }
        }
    }
}