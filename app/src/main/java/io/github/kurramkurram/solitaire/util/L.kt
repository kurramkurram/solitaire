package io.github.kurramkurram.solitaire.util

import android.util.Log
import io.github.kurramkurram.solitaire.BuildConfig

/**
 * ログクラス.
 */
class L {
    companion object {

        /**
         * Debugログ.
         *
         * @param tag タグ
         * @param msg メッセージ
         */
        fun d(tag: String, msg: String) {
            if (BuildConfig.DEBUG) {
                Log.d("Solitaire", "$tag$msg")
            }
        }

        /**
         * Errorログ.
         *
         * @param tag タグ
         * @param msg メッセージ
         */
        fun e(tag: String, msg: String) {
            if (BuildConfig.DEBUG) {
                Log.e("Solitaire", "$tag$msg")
            }
        }
    }
}
