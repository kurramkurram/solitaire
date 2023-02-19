package io.github.kurramkurram.solitaire.util

import android.util.Log
import io.github.kurramkurram.solitaire.BuildConfig

class L {
    companion object {
        fun d(tag: String, msg: String) {
            if (BuildConfig.DEBUG) {
                Log.d("Solitaire", "$tag$msg")
            }
        }

        fun e(tag: String, msg: String) {
            if (BuildConfig.DEBUG) {
                Log.e("Solitaire", "$tag$msg")
            }
        }
    }
}