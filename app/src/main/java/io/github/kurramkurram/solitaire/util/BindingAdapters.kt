package io.github.kurramkurram.solitaire.util

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @BindingAdapter("layout_height")
    @JvmStatic
    fun setLayoutHeight(view: View, height: Int) {
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = height
        view.layoutParams = layoutParams
    }
}