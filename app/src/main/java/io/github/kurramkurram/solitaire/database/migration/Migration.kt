package io.github.kurramkurram.solitaire.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CRATE TABLE t_movie (" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " filename TEXT NOT NULL " +
                    " path TEXT NOT NULL" +
                    ");"
        )
    }
}