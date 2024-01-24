package io.github.kurramkurram.solitaire.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kurramkurram.solitaire.data.Record
import io.github.kurramkurram.solitaire.repository.RecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 移動回数画面のViewModel.
 */
@HiltViewModel
class RecordViewModel @Inject constructor(
    application: Application,
    private val recordRepository: RecordRepository
) : AndroidViewModel(application) {

    /**
     * 表示する記録情報.
     */
    val recordList: LiveData<MutableList<Record>> = recordRepository.getAllSuccess()

    /**
     * 削除する.
     */
    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            recordRepository.deleteAll()
        }
    }
}
