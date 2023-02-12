package io.github.kurramkurram.solitaire.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.data.TrumpCard
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

    @BindingAdapter(value = ["context", "card", "side"], requireAll = true)
    @JvmStatic
    @SuppressLint("UseCompatLoadingForDrawables")
    fun setCardSrc(view: ImageView, context: Context, card: TrumpCard?, side: SIDE?) {
        val drawableId = when {
            card == null || side == null || side == SIDE.BACK -> R.drawable.trump_background

            card.number == NUMBER.NONE -> R.drawable.ic_launcher_foreground

            else -> {
                val array = context.resources.obtainTypedArray(R.array.trump_card_array)
                val id = array.getResourceId(card.id, 0)
                array.recycle()
                id
            }
        }
        view.setImageDrawable(context.resources.getDrawable(drawableId, null))
    }
}