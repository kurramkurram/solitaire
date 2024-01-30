package io.github.kurramkurram.solitaire.viewmodel

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.data.BarChartData
import io.github.kurramkurram.solitaire.data.PieChartData
import io.github.kurramkurram.solitaire.repository.DateRepository
import io.github.kurramkurram.solitaire.repository.RecordRepository
import io.github.kurramkurram.solitaire.usecase.CreateBarChartUseCase
import javax.inject.Inject

/**
 * グラフのViewModel
 */
@HiltViewModel
class GraphViewModel @Inject constructor(
    private val application: Application,
    dateRepository: DateRepository,
    recordRepository: RecordRepository,
    private val createBarChartUseCase: CreateBarChartUseCase,
) : AndroidViewModel(application) {

    private val endDate = dateRepository.getToday()
    private val targetDays = dateRepository.getAWeekDays(endDate)
    val barChartData = recordRepository.selectCountPerDay(targetDays.last(), targetDays[0])
    val pieChartData = recordRepository.selectCountPerTime(targetDays.last(), targetDays[0])

    private val _spinnerItemLiveData = MutableLiveData(0)
    val spinnerItemLiveData: LiveData<Int>
        get() = _spinnerItemLiveData
    var spinnerSelectedItem: Int = 0
        set(value) {
            _spinnerItemLiveData.value = value
            field = value
        }

    private val _barData = MutableLiveData<BarData>(null)
    val barData: LiveData<BarData>
        get() = _barData

    private val _pieData = MutableLiveData<PieData>(null)
    val pieData: LiveData<PieData>
        get() = _pieData

    private val _labelList = mutableListOf<String>()
    val labelList: MutableList<String>
        get() = _labelList

    private val _textAverageValue = MutableLiveData("")
    val textAverageValue: LiveData<String>
        get() = _textAverageValue

    private val _textDuration = MutableLiveData("")
    val textDuration: LiveData<String>
        get() = _textDuration

    /**
     * 棒グラフのデータを作成する.
     *
     * @param data グラフ表示用のデータ
     */
    fun createBarChartData(data: MutableList<BarChartData>) {
        val (label, barChartDataList, average) = createBarChartUseCase.invoke(data, targetDays)

        val resources = application.applicationContext.resources

        _textAverageValue.value =
            resources.getString(R.string.graph_average_value, average.toString())
        _textDuration.value =
            resources.getString(R.string.graph_duration, targetDays.last(), targetDays[0])

        _labelList.apply {
            clear()
            addAll(label)
        }

        val labelText = resources.getString(R.string.graph_title_success_count)
        val barDataSet = BarDataSet(barChartDataList, labelText).apply {
            setDrawValues(false)
            color = Color.WHITE
        }
        val barDataSets = mutableListOf<IBarDataSet>()
        barDataSets.add(barDataSet)
        _barData.value = BarData(barDataSets)
    }

    /**
     * 円グ。ラフのデータを作成する.
     *
     * @param data グラフ表示用のデータ
     */
    fun createPieChartData(data: MutableList<PieChartData>) {
        val resources = application.applicationContext.resources

        val entryList = mutableListOf<PieEntry>()
        var sum = 0
        var success = 0
        data.forEach {
            val label = resources.getString(R.string.graph_time_value, it.t.toString())
            val count = it.count
            entryList.add(PieEntry(count.toFloat(), label))
            sum += it.t * count
            success += count
        }

        val average = if (data.size > 0) {
            sum / 10 / success * 10 // 10秒単位で計算するため先に10で割る
        } else {
            0
        }.toString()
        _textAverageValue.value = resources.getString(R.string.graph_time_value, average)
        _textDuration.value =
            resources.getString(R.string.graph_duration, targetDays.last(), targetDays[0])
        val labelText = resources.getString(R.string.graph_title_success_time)
        val pieDataSet = PieDataSet(entryList, labelText).apply {
            colors = createColorsList(data.size)
            setDrawValues(false)
            valueTextSize = resources.getDimension(R.dimen.graph_value_text_size)
        }
        _pieData.value = PieData(pieDataSet)
    }

    private fun createColorsList(size: Int): List<Int> {
        val colorList = mutableListOf<Int>()
        for (i in 1..size) {
            val r = (0..255).random()
            val g = (0..255).random()
            val b = (0..255).random()
            val color = Color.rgb(r, g, b)
            colorList.add(color)
        }
        return colorList
    }
}
