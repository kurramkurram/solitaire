package io.github.kurramkurram.solitaire.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import io.github.kurramkurram.solitaire.util.L

class RestartFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val alertDialog = AlertDialog.Builder(activity).apply {
            setTitle("タイトル")
            setMessage("ここにメッセージ")
            setPositiveButton("ok") { _, _ ->
                L.d(TAG, "#onCreateDialog positive click")
            }
        }.create()

        return alertDialog
    }

    companion object {
        const val TAG = "RestartFragment"
    }
}