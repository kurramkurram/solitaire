package io.github.kurramkurram.solitaire.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.kurramkurram.solitaire.data.Record

@Dao
interface RecordDao {

    @Query("SELECT * FROM t_record")
    suspend fun selectAll(): MutableList<Record>

    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY count, time ASC")
    fun getSuccess(): LiveData<MutableList<Record>>

    @Query("SELECT (SELECT count(*) FROM t_record WHERE result = 1) * 100 / (SELECT CASE WHEN COUNT(*) = 0 THEN 1 ELSE COUNT(*) END AS RESULT FROM t_record)")
    fun getSuccessRate(): LiveData<Int>

    @Query("SELECT count(*) FROM t_record WHERE result = 1")
    fun getCountSuccess(): LiveData<Int>

    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY time DESC Limit 1")
    fun getLatest(): LiveData<Record>

    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY time ASC Limit 1")
    fun getOldest(): LiveData<Record>

    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY count ASC Limit 1")
    fun getSmallest(): LiveData<Record>

    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY count DESC Limit 1")
    fun getLargest(): LiveData<Record>

    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY time ASC Limit 1")
    fun getShortest(): LiveData<Record>

    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY time DESC Limit 1")
    fun getLongest(): LiveData<Record>

    @Insert
    fun insert(vararg record: Record)

    @Insert
    fun insert(records: List<Record>)

    @Query("DELETE FROM t_record")
    fun deleteAll()
}