package io.github.kurramkurram.solitaire.repository

import android.content.Context
import androidx.lifecycle.LiveData
import io.github.kurramkurram.solitaire.data.Record
import io.github.kurramkurram.solitaire.database.RecordDatabase
import kotlinx.coroutines.*

abstract class RecordRepository {

    /**
     * 全件取得する.
     */
    abstract fun selectAll(): LiveData<MutableList<Record>>

    /**
     * 最新の成功を取得する.
     */
    abstract fun selectLatest(): LiveData<Record>

    /**
     * 最古の成功を取得する.
     */
    abstract fun selectOldest(): LiveData<Record>

    /**
     * 移動回数が最小.
     */
    abstract fun selectSmallest(): LiveData<Record>

    /**
     * 移動回数が最多.
     */
    abstract fun selectLargest(): LiveData<Record>

    /**
     * プレイ時間が最短.
     */
    abstract fun selectShortest(): LiveData<Record>

    /**
     * プレイ時間が最長.
     */
    abstract fun selectLongest(): LiveData<Record>

    /**
     * 保存する.
     */
    abstract fun saveRecord(record: Record)

    /**
     * 全件削除する.
     */
    abstract fun deleteAll()
}

class RecordRepositoryImpl(
    private val context: Context,
    private val db: RecordDatabase = RecordDatabase.getDatabases(context)
) : RecordRepository() {

    override fun selectAll(): LiveData<MutableList<Record>> {
        val dao = db.recordDao()
        return dao.getAll()
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

    override fun saveRecord(record: Record) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = db.recordDao()
            dao.insert(record)
        }
    }

    override fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = db.recordDao()
            dao.deleteAll()
        }
    }
}