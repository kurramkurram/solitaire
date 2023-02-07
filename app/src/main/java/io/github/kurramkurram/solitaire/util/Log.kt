package io.github.kurramkurram.solitaire.util

import android.util.Log

class L {
    companion object {
        fun d(tag: String, msg: String) {
            Log.d("Solitaire", "$tag$msg")
        }

        fun e(tag: String, msg: String) {
            Log.e("Solitaire", "$tag$msg")
        }
    }
}