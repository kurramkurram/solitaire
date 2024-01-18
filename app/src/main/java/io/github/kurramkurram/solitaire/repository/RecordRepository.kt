package io.github.kurramkurram.solitaire.repository

import android.content.Context
import androidx.lifecycle.LiveData
import io.github.kurramkurram.solitaire.data.BarChartData
import io.github.kurramkurram.solitaire.data.PieChartData
import io.github.kurramkurram.solitaire.data.Record
import io.github.kurramkurram.solitaire.database.RecordDatabase
import io.github.kurramkurram.solitaire.util.L
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 記録のリポジトリ.
 */
abstract class RecordRepository {

    /**
     * 成功率を取得.
     *
     * @return 成功率
     */
    abstract fun getSuccessRate(): LiveData<Int>

    /**
     * 成功数を取得.
     *
     * @return 成功件数
     */
    abstract fun getSuccessCount(): LiveData<Int>

    /**
     * 全件取得する.
     *
     * @return 記録情報
     */
    abstract fun getAllSuccess(): LiveData<MutableList<Record>>

    /**
     * 最新の成功を取得する.
     *
     * @return 記録情報
     */
    abstract fun selectLatest(): LiveData<Record>

    /**
     * 最古の成功を取得する.
     *
     * @return 記録情報
     */
    abstract fun selectOldest(): LiveData<Record>

    /**
     * 移動回数が最小.
     *
     * @return 記録情報
     */
    abstract fun selectSmallest(): LiveData<Record>

    /**
     * 移動回数が最多.
     *
     * @return 記録情報
     */
    abstract fun selectLargest(): LiveData<Record>

    /**
     * プレイ時間が最短.
     *
     * @return 記録情報
     */
    abstract fun selectShortest(): LiveData<Record>

    /**
     * プレイ時間が最長.
     *
     * @return 記録情報
     */
    abstract fun selectLongest(): LiveData<Record>

    /**
     * 成功数を日別に取得する.
     */
    abstract fun selectCountPerDay(
        startDate: String,
        endDate: String
    ): LiveData<MutableList<BarChartData>>

    /**
     * 成功数を10秒単位で取得する.
     */
    abstract fun selectCountPerTime(
        startDate: String,
        endDate: String
    ): LiveData<MutableList<PieChartData>>

    /**
     * 保存する.
     *
     * @param record 記録情報
     */
    abstract fun saveRecord(record: Record)

    /**
     * 保存する（複数レコード）.
     *
     * @param record 記録情報
     */
    abstract suspend fun saveRecord(record: List<Record>)

    /**
     * 全件削除する.
     */
    abstract suspend fun deleteAll()
}

class RecordRepositoryImpl(
    private val context: Context,
    private val db: RecordDatabase = RecordDatabase.getDatabases(context)
) : RecordRepository() {

    override fun getSuccessCount(): LiveData<Int> {
        val dao = db.recordDao()
        return dao.getCountSuccess()
    }

    override fun getSuccessRate(): LiveData<Int> {
        val dao = db.recordDao()
        return dao.getSuccessRate()
    }

    override fun getAllSuccess(): LiveData<MutableList<Record>> {
        val dao = db.recordDao()
        return dao.getSuccess()
    }

    override fun selectLatest(): LiveData<Record> {
        val dao = db.recordDao()
        return dao.getLatest()
    }

    override fun selectOldest(): LiveData<Record> {
        val dao = db.recordDao()
        return dao.getOldest()
    }

    override fun selectSmallest(): LiveData<Record> {
        val dao = db.recordDao()
        return dao.getSmallest()
    }

    override fun selectLargest(): LiveData<Record> {
        val dao = db.recordDao()
        return dao.getLargest()
    }

    override fun selectShortest(): LiveData<Record> {
        val dao = db.recordDao()
        return dao.getShortest()
    }

    override fun selectLongest(): LiveData<Record> {
        val dao = db.recordDao()
        return dao.getLongest()
    }

    override fun selectCountPerDay(
        startDate: String,
        endDate: String
    ): LiveData<MutableList<BarChartData>> {
        L.d("RecordRepository", "#selectCountPerDay")
        val dao = db.recordDao()
        return dao.getBarChartData(startDate, endDate)
    }

    override fun selectCountPerTime(
        startDate: String,
        endDate: String
    ): LiveData<MutableList<PieChartData>> {
        val dao = db.recordDao()
        return dao.getPieChartData(startDate, endDate)
    }

    override fun saveRecord(record: Record) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = db.recordDao()
            dao.insert(record)
        }
    }

    override suspend fun saveRecord(record: List<Record>) {
        val dao = db.recordDao()
        dao.insert(record)
    }

    override suspend fun deleteAll() {
        val dao = db.recordDao()
        dao.deleteAll()
    }
}
