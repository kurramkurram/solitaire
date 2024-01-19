package io.github.kurramkurram.solitaire.util

import android.graphics.Color
import com.github.mikephil.charting.charts.Chart

/**
 * グラフの共通のスタイルを適用する.
 */
fun Chart<*>.applyChartStyle() {
    legend.isWordWrapEnabled = true
    legend.textColor = Color.WHITE
    description.isEnabled = false
    animateY(1000)
    setTouchEnabled(false)
}
