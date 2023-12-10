package io.github.kurramkurram.solitaire.repository

import android.content.Context
import io.github.kurramkurram.solitaire.database.TmpRecordDatabase
import io.github.kurramkurram.solitaire.data.Record

abstract class TmpRecordRepository {
    abstract suspend fun selectAll(): MutableList<Record>

    abstract suspend fun insert(record: Record)
}

class TmpRecordRepositoryImpl(
    context: Context,
    private val db: TmpRecordDatabase = TmpRecordDatabase.getDatabases(context)
) : TmpRecordRepository() {

    override suspend fun selectAll(): MutableList<Record> {
        val dao = db.recordDao()
        return dao.selectAll()
    }

    override suspend fun insert(record: Record) {
        val dao = db.recordDao()
        return dao.insert(record)
    }


}