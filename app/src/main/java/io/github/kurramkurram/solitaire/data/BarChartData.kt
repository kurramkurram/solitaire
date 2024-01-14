package io.github.kurramkurram.solitaire.data

import androidx.room.ColumnInfo

data class BarChartData(
    val day: String,
    @ColumnInfo(name = "count(*)") val count: Int
)
