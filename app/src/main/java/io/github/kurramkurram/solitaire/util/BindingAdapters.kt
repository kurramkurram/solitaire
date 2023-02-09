package io.github.kurramkurram.solitaire.util

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import kotlinx.android.synthetic.main.dialog_restart.view.*

object BindingAdapters {

    @BindingAdapter("layout_height")
    @JvmStatic
    fun setLayoutHeight(view: View, height: Int) {
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = height
        view.layoutParams = layoutParams
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter("text")
    @JvmStatic
    fun setTimeText(view: TextView, time: Long) {
        val minute = time / 60
        val second = time % 60
        view.text = "${String.format("%02d", minute)}:${String.format("%02d", second)}"
    }
}