package io.github.kurramkurram.solitaire.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.util.*
import kotlinx.android.synthetic.main.dialog_restart.*

class DialogRestartFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_restart)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            positive_button.setOnClickListener { onPositiveClick() }
            negative_button.setOnClickListener { onNegativeClick() }
        }

    private fun onPositiveClick() {
        val data = Bundle()
        data.putInt(DIALOG_RESULT_KEY, DIALOG_RESULT_OK)
        parentFragmentManager.setFragmentResult(DIALOG_RESULT, data)
        dismiss()
    }

    private fun onNegativeClick() {
        val data = Bundle()
        data.putInt(DIALOG_RESULT_KEY, DIALOG_RESULT_NG)
        parentFragmentManager.setFragmentResult(DIALOG_RESULT, data)
        dismiss()
    }
}