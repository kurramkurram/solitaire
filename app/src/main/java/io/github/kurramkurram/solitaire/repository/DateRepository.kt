package io.github.kurramkurram.solitaire.repository

import io.github.kurramkurram.solitaire.database.DateDataSource
import io.github.kurramkurram.solitaire.database.DateDataSourceImpl
import java.util.Calendar

/**
 * 日付に関するRepository.
 */
abstract class DateRepository {

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

class DateRepositoryImpl(
    private val dateDataSource: DateDataSource = DateDataSourceImpl()
) : DateRepository() {

    override fun getToday(): Calendar = dateDataSource.getToday()

    override fun getAWeekDays(endDate: Calendar): List<String> =
        dateDataSource.getAWeekDays(endDate)
}
