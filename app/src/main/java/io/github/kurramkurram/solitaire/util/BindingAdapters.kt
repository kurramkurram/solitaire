package io.github.kurramkurram.solitaire.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.databinding.BindingAdapter
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.data.TrumpCard
import io.github.kurramkurram.solitaire.view.SettingItem

object BindingAdapters {

    /**
     * 場札のカードの高さ指定.
     *
     * @param view 対象のView
     * @param height 高さ
     */
    @BindingAdapter("layout_height")
    @JvmStatic
    fun setLayoutHeight(view: View, height: Int) {
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = height
        view.layoutParams = layoutParams
    }

    /**
     * 経過時刻を表示.
     *
     * @param view 対象のTextView
     * @param time 時刻
     */
    @SuppressLint("SetTextI18n")
    @BindingAdapter("text")
    @JvmStatic
    fun setTimeText(view: TextView, time: Long) {
        val minute = time / 60
        val second = time % 60
        view.text = "${String.format("%02d", minute)}:${String.format("%02d", second)}"
    }

    /**
     * 記録画面の順位の色を決める.
     *
     * @param view 対象のTextView
     * @param context [Context]
     * @param rank 順位
     */
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

    /**
     * カードを表示する.
     *
     * @param view 対象のImageView
     * @param context [Context]
     * @param card [TrumpCard]
     * @param side [SIDE]
     */
    @BindingAdapter(value = ["context", "card", "side"], requireAll = true)
    @JvmStatic
    fun setCardSrc(view: ImageView, context: Context, card: TrumpCard?, side: SIDE?) {
        val drawableId = when {
            card == null || side == null || side == SIDE.BACK -> R.drawable.trump_background

            card.number == NUMBER.NONE -> {
                when (card.pattern) {
                    PATTERN.SPADE -> R.drawable.spade
                    PATTERN.HEART -> R.drawable.heart
                    PATTERN.CLOVER -> R.drawable.clover
                    PATTERN.DIAMOND -> R.drawable.diamond
                }
            }

            else -> {
                val array = context.resources.obtainTypedArray(R.array.trump_card_array)
                val id = array.getResourceId(card.id, 0)
                array.recycle()
                id
            }
        }
        view.setImageDrawable(ResourcesCompat.getDrawable(context.resources, drawableId, null))
    }

    /**
     * カードのpaddingを指定する.
     *
     * @param view 対象のImageView
     * @param card [TrumpCard]
     * @param side [SIDE]
     */
    @BindingAdapter(value = ["padding_card", "padding_side"], requireAll = true)
    @JvmStatic
    fun setFoundationPadding(view: ImageView, card: TrumpCard?, side: SIDE?) {
        val padding = when {
            card == null || side == null -> 0
            card.number == NUMBER.NONE -> 50
            else -> 0
        }
        view.setPadding(padding)
    }

    /**
     * 表になっている山札を指定する.
     *
     * @param view 対象のImageView
     * @param context [Context]
     * @param cardList [TrumpCard]のリスト
     * @param index 表になっているカードのインデックス
     */
    @BindingAdapter(value = ["open_context", "open_card_list", "open_index"], requireAll = true)
    @JvmStatic
    fun setStockOpenCard(
        view: ImageView,
        context: Context,
        cardList: List<TrumpCard>?,
        index: Int
    ) {
        val drawableId = when {
            cardList == null || cardList.size <= index || cardList.isEmpty() ||
                cardList[cardList.size - index - 1].number == NUMBER.NONE -> R.color.background_green

            else -> {
                view.setPadding(0)
                val array = context.resources.obtainTypedArray(R.array.trump_card_array)
                val id = array.getResourceId(cardList[cardList.size - index - 1].id, 0)
                array.recycle()
                id
            }
        }
        view.setImageDrawable(ResourcesCompat.getDrawable(context.resources, drawableId, null))
    }

    /**
     * 裏になっている山札を指定する.
     *
     * @param view 対象のImageView
     * @param context [Context]
     * @param card [TrumpCard]
     */
    @BindingAdapter(value = ["close_context", "close_card"], requireAll = true)
    @JvmStatic
    fun setStockCloseCard(view: ImageView, context: Context, card: TrumpCard?) {
        val id = if (card == null) {
            view.setPadding(25)
            R.drawable.restart_sotck_button
        } else {
            view.setPadding(0)
            R.drawable.trump_background
        }
        view.setImageDrawable(ResourcesCompat.getDrawable(context.resources, id, null))
    }

    /**
     * 設定画面の項目に値を指定.
     *
     * @param view 設定画面の項目
     * @param icon 表示するアイコン
     * @param title タイトル
     * @param description 説明
     * @param hasArrow 矢印の有無
     * @param switch トグルスイッチ
     */
    @BindingAdapter(
        value = ["setting_icon", "setting_title", "setting_description", "setting_hasArrow", "setting_switch"],
        requireAll = false
    )
    @JvmStatic
    fun setSettingItem(
        view: SettingItem,
        icon: Drawable? = null,
        title: String = "",
        description: String? = "",
        hasArrow: Boolean = false,
        switch: Boolean?
    ) = view.setValue(icon, title, description, hasArrow, switch)

    /**
     * 完了テキストの表示アニメーション.
     *
     * @param view
     * @param visibility 表示・非表示
     */
    @BindingAdapter("visibility")
    @JvmStatic
    fun setVisibility(view: TextView, visibility: Int) {
        if (view.visibility == visibility) return
        if (visibility == View.GONE) {
            view.visibility = visibility
            return
        }

        val duration = 800L
        val startOffset = 100L

        val fromAlpha = 0f
        val toAlpha = 1f
        val alpha = AlphaAnimation(fromAlpha, toAlpha).apply {
            view.visibility = View.VISIBLE
            this.duration = duration
            this.startOffset = startOffset
        }

        val fromScale = 0f
        val toScale = 1f
        val pivotValue = 0.5f
        val scale = ScaleAnimation(
            fromScale,
            toScale,
            fromScale,
            toScale,
            Animation.RELATIVE_TO_SELF,
            pivotValue,
            Animation.RELATIVE_TO_SELF,
            pivotValue
        ).apply {
            this.duration = duration
            this.startOffset = startOffset
        }

        val set = AnimationSet(true).apply {
            interpolator = AccelerateInterpolator()
            addAnimation(alpha)
            addAnimation(scale)
        }
        view.startAnimation(set)
    }
}
