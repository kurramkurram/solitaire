package io.github.kurramkurram.solitaire.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import io.github.kurramkurram.solitaire.R
import kotlinx.android.synthetic.main.dialog_reset.*

class DialogResetFragment : DialogBaseFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_reset)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            positive_button.setOnClickListener { onPositiveClick() }
            negative_button.setOnClickListener { onNegativeClick() }
        }
}