package io.github.kurramkurram.solitaire.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.util.*

class DialogRestartFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val alertDialog = AlertDialog.Builder(activity).apply {
            val data = Bundle()
            setTitle(resources.getString(R.string.restart_dialog_title))
            setMessage(resources.getString(R.string.restart_dialog_text))
            setIcon(ResourcesCompat.getDrawable(resources, R.drawable.restart_dark, null))
            setPositiveButton(resources.getString(R.string.restart_dialog_positive)) { _, _ ->
                data.putInt(DIALOG_RESULT_KEY, DIALOG_RESULT_OK)
                parentFragmentManager.setFragmentResult(DIALOG_RESULT, data)
            }
            setNegativeButton(resources.getString(R.string.restart_dialog_negative)) { _, _ ->
                data.putInt(DIALOG_RESULT_KEY, DIALOG_RESULT_NG)
                parentFragmentManager.setFragmentResult(DIALOG_RESULT, data)
            }
        }.create()

        return alertDialog
    }
}