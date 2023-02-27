package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_KEY
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_NG
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_OK

open class DialogBaseFragment : DialogFragment() {

    fun onPositiveClick() {
        val data = Bundle()
        data.putInt(DIALOG_RESULT_KEY, DIALOG_RESULT_OK)
        parentFragmentManager.setFragmentResult(DIALOG_RESULT, data)
        dismiss()
    }

     fun onNegativeClick() {
        val data = Bundle()
        data.putInt(DIALOG_RESULT_KEY, DIALOG_RESULT_NG)
        parentFragmentManager.setFragmentResult(DIALOG_RESULT, data)
        dismiss()
    }
}