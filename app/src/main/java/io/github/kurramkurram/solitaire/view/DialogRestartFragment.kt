package io.github.kurramkurram.solitaire.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import io.github.kurramkurram.solitaire.util.*

class DialogRestartFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val alertDialog = AlertDialog.Builder(activity).apply {
            val data = Bundle()

            setTitle("リセット")
            setMessage("リセットしますか？")
            setPositiveButton("OK") { _, _ ->
                data.putInt(DIALOG_RESULT_KEY, DIALOG_RESULT_OK)
                parentFragmentManager.setFragmentResult(DIALOG_RESULT, data)
            }
            setNegativeButton("No") { _, _ ->
                data.putInt(DIALOG_RESULT_KEY, DIALOG_RESULT_NG)
                parentFragmentManager.setFragmentResult(DIALOG_RESULT, data)
            }
        }.create()

        return alertDialog
    }

    companion object {
        const val TAG = "RestartFragment"
    }
}