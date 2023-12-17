package io.github.kurramkurram.solitaire.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.kurramkurram.solitaire.dao.RecordDao
import io.github.kurramkurram.solitaire.data.Record


@Database(entities = [Record::class], version = 1, exportSchema = false)
abstract class TmpRecordDatabase : RoomDatabase() {

    abstract fun recordDao(): RecordDao

    companion object {
        const val DB_NAME = "TmpRecord.db"

        @Volatile
        private var INSTANCE: TmpRecordDatabase? = null

        fun getDatabases(context: Context): TmpRecordDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) return tmpInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TmpRecordDatabase::class.java,
                    DB_NAME
                ).setJournalMode(JournalMode.TRUNCATE).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}