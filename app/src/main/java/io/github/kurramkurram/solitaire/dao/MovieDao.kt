package io.github.kurramkurram.solitaire.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.kurramkurram.solitaire.data.Movie

/**
 * 動画DataAccessObject.
 */
@Dao
interface MovieDao {

    /**
     * 書き込み.
     *
     * @param movie 動画情報
     */
    @Insert
    fun insert(movie: Movie)

    /**
     * 動画の件数を取得する.
     *
     * @return 件数
     */
    @Query("SELECT count(*) FROM t_movie")
    fun selectCount(): Int

    /**
     * 動画情報を降順に取得する.
     *
     * @return 動画情報リスト
     */
    @Query("SELECT * FROM t_movie ORDER BY id DESC")
    fun selectAll(): LiveData<MutableList<Movie>>

    /**
     * ファイル名を取得する.
     *
     * @return ファイル名一覧
     */
    @Query("SELECT fileName FROM t_movie")
    fun selectAllFileName(): MutableList<String>

    /**
     * テーブルを削除する.
     */
    @Query("DELETE FROM t_movie")
    fun deleteAll()

    /**
     * 削除対象のファイル名を取得する.
     *
     * @return ファイル名
     */
    @Query("SELECT fileName FROM t_movie ORDER BY id ASC LIMIT 1")
    fun selectDeleteMovie(): String

    /**
     * 最も古いものを削除する.
     *
     * @param name ファイル名
     */
    @Query("DELETE FROM t_movie WHERE fileName = :name")
    fun deleteOldest(name: String)
}
