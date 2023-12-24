package io.github.kurramkurram.solitaire.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.kurramkurram.solitaire.dao.RecordDao
import io.github.kurramkurram.solitaire.data.Record
import io.github.kurramkurram.solitaire.database.migration.MIGRATION_1_2

@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class RecordDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao

    companion object {
        const val DB_NAME = "Record.db"

        @Volatile
        private var INSTANCE: RecordDatabase? = null

        fun getDatabases(context: Context): RecordDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) return tmpInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecordDatabase::class.java,
                    DB_NAME
                ).setJournalMode(JournalMode.TRUNCATE).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}