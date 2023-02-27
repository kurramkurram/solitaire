package io.github.kurramkurram.solitaire.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import io.github.kurramkurram.solitaire.R
import kotlinx.android.synthetic.main.dialog_restart.*

class DialogRestartFragment : DialogBaseFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_restart)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            positive_button.setOnClickListener { onPositiveClick() }
            negative_button.setOnClickListener { onNegativeClick() }
        }
}