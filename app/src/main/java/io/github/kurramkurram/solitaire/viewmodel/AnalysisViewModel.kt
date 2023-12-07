package io.github.kurramkurram.solitaire.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.kurramkurram.solitaire.repository.RecordRepositoryImpl
import io.github.kurramkurram.solitaire.data.Record

class AnalysisViewModel(application: Application) : AndroidViewModel(application) {
    private val recordRepository = RecordRepositoryImpl(application.applicationContext)

    /**
     * 成功回数.
     */
    val successCount: LiveData<Int> = recordRepository.getSuccessCount()

    /**
     * 成功率.
     */
    val successRate: LiveData<Int> = recordRepository.getSuccessRate()

    /**
     * 最新の成功.
     */
    val successLatest: LiveData<Record> = recordRepository.selectLatest()

    /**
     * 最古の成功.
     */
    val successOldest: LiveData<Record> = recordRepository.selectOldest()

    /**
     * 最小移動の成功.
     */
    val successSmallest: LiveData<Record> = recordRepository.selectSmallest()

    /**
     * 最多移動の成功.
     */
    val successLargest: LiveData<Record> = recordRepository.selectLargest()

    /**
     * 最短の成功.
     */
    val successShortest: LiveData<Record> = recordRepository.selectShortest()

    /**
     * 最長の成功.
     */
    val successLongest: LiveData<Record> = recordRepository.selectLongest()
}