package io.github.kurramkurram.solitaire.data

import androidx.room.ColumnInfo

/**
 * 円グラフ表示用のデータ.
 *
 * @param day 日付
 * @param t 成功までの時間（10秒単位）
 * @param count 件数
 */
data class PieChartData(
    val day: String,
    val t: Int,
    @ColumnInfo(name = "count(*)") val count: Int
)
