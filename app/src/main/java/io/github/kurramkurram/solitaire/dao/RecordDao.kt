package io.github.kurramkurram.solitaire.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.kurramkurram.solitaire.data.BarChartData
import io.github.kurramkurram.solitaire.data.PieChartData
import io.github.kurramkurram.solitaire.data.Record

/**
 * 記録のDataAccessObject.
 */
@Dao
interface RecordDao {

    /**
     * 全件取得.
     *
     * @return 記録全件
     */
    @Query("SELECT * FROM t_record")
    suspend fun selectAll(): MutableList<Record>

    /**
     * 成功の記録を取得.
     *
     * @return 成功全件
     */
    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY count, time ASC")
    fun getSuccess(): LiveData<MutableList<Record>>

    /**
     * 成功率を取得する.
     *
     * @return 成功率
     */
    @Query("SELECT (SELECT count(*) FROM t_record WHERE result = 1) * 100 / (SELECT CASE WHEN COUNT(*) = 0 THEN 1 ELSE COUNT(*) END AS RESULT FROM t_record)")
    fun getSuccessRate(): LiveData<Int>

    /**
     * 成功件数を取得する.
     *
     * @return 成功件数
     */
    @Query("SELECT count(*) FROM t_record WHERE result = 1")
    fun getCountSuccess(): LiveData<Int>

    /**
     * 最新の成功を取得する.
     *
     * @return 最新の成功記録
     */
    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY date DESC Limit 1")
    fun getLatest(): LiveData<Record>

    /**
     * もっとも古い成功を取得する.
     *
     * @return もっとも古い成功記録
     */
    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY date ASC Limit 1")
    fun getOldest(): LiveData<Record>

    /**
     * 最小の移動回数の成功を取得する.
     *
     * @return 最小の移動の成功記録
     */
    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY count ASC Limit 1")
    fun getSmallest(): LiveData<Record>

    /**
     * 最大の移動回数の成功を取得する.
     *
     * @return 最大の移動の成功記録
     */
    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY count DESC Limit 1")
    fun getLargest(): LiveData<Record>

    /**
     * 最短の移動回数の成功を取得する.
     *
     * @return 最短の移動の成功記録
     */
    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY time ASC Limit 1")
    fun getShortest(): LiveData<Record>

    /**
     * 最長の移動回数の成功を取得する.
     *
     * @return 最長の移動の成功記録
     */
    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY time DESC Limit 1")
    fun getLongest(): LiveData<Record>

    /**
     * 日別の成功件数を取得.
     *
     * @param startDate 開始日
     * @param endDate 終了日
     * @return 日別の成功件数
     */
    @Query("SELECT substr(date, 0, 11) AS day, count(*) FROM t_record WHERE result = 1 AND day BETWEEN :startDate AND :endDate GROUP BY day")
    fun getBarChartData(startDate: String, endDate: String): LiveData<MutableList<BarChartData>>

    /**
     * 10秒単位の成功件数を取得.
     *
     * @param startDate 開始日
     * @param endDate 終了日
     * @return 10秒単位の成功件数
     */
    @Query("SELECT substr(date, 0, 11) AS day, time / 10 * 10 AS t, count(*) FROM t_record WHERE result = 1 AND day BETWEEN :startDate AND :endDate GROUP BY t")
    fun getPieChartData(startDate: String, endDate: String): LiveData<MutableList<PieChartData>>

    /**
     * 記録の書き込み.
     *
     * @param record 記録
     */
    @Insert
    fun insert(vararg record: Record)

    /**
     * 記録の書き込み（複数）.
     *
     * @param records 記録
     */
    @Insert
    fun insert(records: List<Record>)

    /**
     * テーブルを削除する.
     */
    @Query("DELETE FROM t_record")
    fun deleteAll()
}
