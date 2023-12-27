package io.github.kurramkurram.solitaire.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.kurramkurram.solitaire.data.Movie

@Dao
interface MovieDao {

    @Insert
    fun insert(movie: Movie)

    @Query("SELECT count(*) FROM t_movie")
    fun selectCount(): Int

    @Query("SELECT * FROM t_movie ORDER BY id DESC")
    fun selectAll(): LiveData<MutableList<Movie>>

    @Query("SELECT fileName FROM t_movie")
    fun selectAllFileName(): MutableList<String>

    @Query("DELETE FROM t_movie")
    fun deleteAll()

    @Query("SELECT fileName FROM t_movie ORDER BY id ASC LIMIT 1")
    fun selectDeleteMovie(): String

    @Query("DELETE FROM t_movie WHERE fileName = :name")
    fun deleteOldest(name: String)
}