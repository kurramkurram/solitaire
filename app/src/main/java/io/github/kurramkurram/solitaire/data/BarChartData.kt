package io.github.kurramkurram.solitaire.data

import androidx.room.ColumnInfo

/**
 * 棒グラフ表示データ.
 */
data class BarChartData(
    val day: String,
    @ColumnInfo(name = "count(*)") val count: Int
)
