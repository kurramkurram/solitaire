package io.github.kurramkurram.solitaire.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.github.kurramkurram.solitaire.R
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

    @BindingAdapter(value = ["context", "rank"], requireAll = true)
    @JvmStatic
    fun setRankColor(view: TextView, context: Context, rank: Int) {
        val colorId = when (rank) {
            1 -> R.color.rank_1
            2 -> R.color.rank_2
            3 -> R.color.rank_3
            else -> R.color.white
        }
        val color = context.resources.getColor(colorId, null)
        view.setTextColor(color)
    }
}