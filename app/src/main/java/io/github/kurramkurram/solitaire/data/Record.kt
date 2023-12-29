package io.github.kurramkurram.solitaire.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 記録のデータ.
 */
@Entity(tableName = "t_record")
data class Record(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "result") val result: Boolean,
    @ColumnInfo(name = "count") val count: Int,
    @ColumnInfo(name = "time") val time: Long,
    @ColumnInfo(name = "date") val date: String
)
