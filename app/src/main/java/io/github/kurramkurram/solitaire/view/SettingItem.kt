package io.github.kurramkurram.solitaire.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.kurramkurram.solitaire.databinding.SettingsItemBinding

/**
 * 設定画面の項目.
 */
class SettingItem(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    val binding: SettingsItemBinding = SettingsItemBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null, 0)

    fun setValue(
        icon: Drawable? = null,
        title: String = "",
        description: String? = "",
        hasArrow: Boolean = false,
        switch: Boolean?
    ) {
        icon?.let {
            binding.iconView.apply {
                visibility = View.VISIBLE
                setImageDrawable(icon)
            }
        }

        if (title.isNotEmpty()) binding.titleView.text = title

        description?.let {
            if (it.isNotEmpty()) {
                binding.descriptionView.apply {
                    visibility = View.VISIBLE
                    text = description
                }
            }
        }

        if (hasArrow) binding.arrowView.visibility = View.VISIBLE

        switch?.let {
            binding.switchButton.apply {
                visibility = View.VISIBLE
                isChecked = switch
            }
        }
    }
}
