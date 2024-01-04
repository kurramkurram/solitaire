package io.github.kurramkurram.solitaire.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * 山札の表のカードの重なるカード.
 */
class LeftCropImageView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    AppCompatImageView(context, attrs, defStyle) {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        scaleType = ScaleType.CENTER_CROP
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val result = super.setFrame(l, t, r, b)

        val imageRatio = drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight

        val imageWidth = (height * imageRatio).toInt()

        val offsetX = 0.coerceAtMost((width - imageWidth) / 2)
        imageMatrix.setTranslate(offsetX.toFloat(), 0f)
        return result
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable != null) {
            val matrix: Matrix = imageMatrix
            matrix.setScale(
                height.toFloat() / drawable.intrinsicHeight,
                height.toFloat() / drawable.intrinsicHeight
            )
            imageMatrix = matrix
        }

        // 画像を描画
        super.onDraw(canvas)
    }
}
