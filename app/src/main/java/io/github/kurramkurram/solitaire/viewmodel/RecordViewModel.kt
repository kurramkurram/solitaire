package io.github.kurramkurram.solitaire.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.kurramkurram.solitaire.data.Record
import io.github.kurramkurram.solitaire.repository.RecordRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordViewModel(application: Application) : AndroidViewModel(application) {

    private val recordRepository = RecordRepositoryImpl(application.applicationContext)

    val recordList: LiveData<MutableList<Record>> = recordRepository.selectAll()

    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            recordRepository.deleteAll()
        }
    }
}