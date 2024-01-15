package io.github.kurramkurram.solitaire.database

import android.annotation.SuppressLint
import io.github.kurramkurram.solitaire.util.DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * 日付に関するデータソース.
 */
abstract class DateDataSource {

    /**
     * 今日の日付を取得する.
     *
     * @return Calendar
     */
    abstract fun getToday(): Calendar

    /**
     * 1週間の日付のリストを取得する.
     *
     * @return 日付のリスト
     */
    abstract fun getAWeekDays(endDate: Calendar): List<String>
}

class DateDataSourceImpl : DateDataSource() {

    override fun getToday(): Calendar {
        return Calendar.getInstance()
    }

    @SuppressLint("SimpleDateFormat")
    override fun getAWeekDays(endDate: Calendar): List<String> {
        val list = mutableListOf<String>()
        val sdf = SimpleDateFormat(DATE_PATTERN)
        do {
            val time = sdf.format(endDate.time)
            list.add(time)
            endDate.add(Calendar.DAY_OF_YEAR, -1)
        } while (list.size < 7)
        return list
    }
}
