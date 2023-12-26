package io.github.kurramkurram.solitaire.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_movie")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "fileName") val fileName: String,
    @ColumnInfo(name = "path") val path: String
)
