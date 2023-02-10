package io.github.kurramkurram.solitaire.repository

import android.content.Context
import io.github.kurramkurram.solitaire.data.Record
import io.github.kurramkurram.solitaire.database.RecordDatabase
import kotlinx.coroutines.*

abstract class RecordRepository {

    abstract fun selectAll(): List<Record>
    abstract fun saveRecord(record: Record)
}

class RecordRepositoryImpl(
    private val context: Context,
    private val db: RecordDatabase = RecordDatabase.getDatabases(context)
) : RecordRepository() {

    override fun selectAll(): List<Record> {
        val dao = db.recordDao()
        return dao.getAll()
    }

    override fun saveRecord(record: Record) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = db.recordDao()
            dao.insert(record)
        }
    }
}