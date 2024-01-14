package io.github.kurramkurram.solitaire.viewmodel

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.data.BarChartData
import io.github.kurramkurram.solitaire.repository.DateRepositoryImpl
import io.github.kurramkurram.solitaire.repository.RecordRepositoryImpl
import io.github.kurramkurram.solitaire.usecase.CreateBarChartUseCase

/**
 * グラフのViewModel
 */
class GraphViewModel(private val application: Application) : AndroidViewModel(application) {
    private val recordRepository = RecordRepositoryImpl(application.applicationContext)
    private val dateRepository = DateRepositoryImpl()

    private val endDate = dateRepository.getToday()
    private val targetDays = dateRepository.getAWeekDays(endDate)
    val barChartData = recordRepository.selectCountPerDay(targetDays.last(), targetDays[0])

    private val _barData = MutableLiveData<BarData>(null)
    val barData: LiveData<BarData>
        get() = _barData

    private val _labelList = mutableListOf<String>()
    val labelList: MutableList<String>
        get() = _labelList

    private val _textAverageValue = MutableLiveData("")
    val textAverageValue: LiveData<String>
        get() = _textAverageValue

    private val _textDuration = MutableLiveData("")
    val textDuration: LiveData<String>
        get() = _textDuration

    fun createBarChartData(data: MutableList<BarChartData>) {
        val (label, barChartDataList, sum) = CreateBarChartUseCase(data, targetDays).invoke()

        val resources = application.applicationContext.resources

        _textAverageValue.value =
            resources.getString(R.string.graph_average_value, (sum / targetDays.size).toString())
        _textDuration.value =
            resources.getString(R.string.graph_duration, targetDays.last(), targetDays[0])

        _labelList.apply {
            clear()
            addAll(label)
        }

        val labelText = resources.getString(R.string.graph_title_success)
        val barDataSet = BarDataSet(barChartDataList, labelText).apply {
            setDrawValues(false)
            color = Color.WHITE
        }
        val barDataSets = mutableListOf<IBarDataSet>()
        barDataSets.add(barDataSet)
        _barData.value = BarData(barDataSets)
    }
}
