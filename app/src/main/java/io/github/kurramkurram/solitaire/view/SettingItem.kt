package io.github.kurramkurram.solitaire.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.kurramkurram.solitaire.R
import kotlinx.android.synthetic.main.settings_item.view.*


class SettingItem(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null, 0)

    init {
        View.inflate(context, R.layout.settings_item, this)
    }

    fun setDescription(description: String) {
        description_view.apply {
            visibility = View.VISIBLE
            text = description
        }
    }

    fun setValue(
        icon: Drawable? = null,
        title: String = "",
        description: String? = "",
        hasArrow: Boolean = false,
    ) {
        Log.d("SettingsItem", "#setValue icon = $icon")
        icon?.let {
            icon_view.apply {
                visibility = View.VISIBLE
                setImageDrawable(icon)
            }
        }

        if (title.isNotEmpty()) title_view.text = title

        description?.let {
            if (it.isNotEmpty()) {
                description_view.apply {
                    visibility = View.VISIBLE
                    text = description
                }
            }
        }

        if (hasArrow) arrow_view.visibility = View.VISIBLE
    }
}