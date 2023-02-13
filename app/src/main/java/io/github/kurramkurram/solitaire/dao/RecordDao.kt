package io.github.kurramkurram.solitaire.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.kurramkurram.solitaire.data.Record

@Dao
interface RecordDao {

    @Query("SELECT * FROM t_record WHERE result = 1 ORDER BY count ASC")
    fun getAll(): LiveData<MutableList<Record>>

    @Insert
    fun insert(vararg record: Record)

    @Query("DELETE FROM t_record")
    fun deleteAll()
}