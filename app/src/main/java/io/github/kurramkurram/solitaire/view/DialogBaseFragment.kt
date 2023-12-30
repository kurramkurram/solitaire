package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_KEY
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_NG
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_OK

/**
 * ベースダイアログ.
 */
open class DialogBaseFragment : DialogFragment() {

    fun onPositiveClick(resultKey: String) {
        val data = Bundle()
        data.putInt(DIALOG_RESULT_KEY, DIALOG_RESULT_OK)
        parentFragmentManager.setFragmentResult(resultKey, data)
        dismiss()
    }

    fun onNegativeClick(resultKey: String) {
        val data = Bundle()
        data.putInt(DIALOG_RESULT_KEY, DIALOG_RESULT_NG)
        parentFragmentManager.setFragmentResult(resultKey, data)
        dismiss()
    }
}
