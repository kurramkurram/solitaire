package io.github.kurramkurram.solitaire.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.kurramkurram.solitaire.data.Record

abstract class RecordLocalDataSource {
    abstract val oldestRecord: LiveData<Record>

    abstract fun saveOldestRecord(record: Record)
}

class RecordLocalDataSourceImpl : RecordLocalDataSource() {

    private var _oldestRecord = MutableLiveData<Record>()
    override val oldestRecord: LiveData<Record>
        get() = _oldestRecord

    override fun saveOldestRecord(record: Record) {
        _oldestRecord.value = record
    }
}