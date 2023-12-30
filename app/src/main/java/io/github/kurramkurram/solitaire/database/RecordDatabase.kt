package io.github.kurramkurram.solitaire.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.kurramkurram.solitaire.dao.MovieDao
import io.github.kurramkurram.solitaire.dao.RecordDao
import io.github.kurramkurram.solitaire.data.Movie
import io.github.kurramkurram.solitaire.data.Record
import io.github.kurramkurram.solitaire.database.migration.MIGRATION_1_2

/**
 * メインのDatabase.
 */
@Database(entities = [Record::class, Movie::class], version = 2, exportSchema = false)
abstract class RecordDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao

    abstract fun movieDao(): MovieDao

    companion object {
        const val DB_NAME = "Record.db"

        @Volatile
        private var INSTANCE: RecordDatabase? = null

        /**
         * インスタンスを取得する.
         *
         * @param context [Context]
         * @return [RecordDatabase]
         */
        fun getDatabases(context: Context): RecordDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) return tmpInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecordDatabase::class.java,
                    DB_NAME
                ).apply {
                    allowMainThreadQueries()
                    addMigrations(MIGRATION_1_2)
                    setJournalMode(JournalMode.TRUNCATE)
                }.build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
