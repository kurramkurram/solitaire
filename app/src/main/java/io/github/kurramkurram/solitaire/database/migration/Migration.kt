package io.github.kurramkurram.solitaire.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS t_movie (" +
                " id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                " fileName TEXT NOT NULL, " +
                " path TEXT NOT NULL" +
                ");"
        )
    }
}
