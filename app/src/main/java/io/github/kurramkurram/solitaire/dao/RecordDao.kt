package io.github.kurramkurram.solitaire.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.kurramkurram.solitaire.data.Record

@Dao
interface RecordDao {

    @Query("SELECT * FROM t_record")
    fun getAll(): List<Record>

    @Insert
    fun insert(vararg record: Record)
}