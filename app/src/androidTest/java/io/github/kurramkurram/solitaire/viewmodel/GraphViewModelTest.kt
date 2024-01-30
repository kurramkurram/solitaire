package io.github.kurramkurram.solitaire.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mikephil.charting.data.BarEntry
import io.github.kurramkurram.solitaire.data.BarChartData
import io.github.kurramkurram.solitaire.data.PieChartData
import io.github.kurramkurram.solitaire.data.Record
import io.github.kurramkurram.solitaire.repository.DateRepository
import io.github.kurramkurram.solitaire.repository.RecordRepository
import io.github.kurramkurram.solitaire.usecase.CreateBarChartUseCase
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class GraphViewModelTest {

    @Test
    @UiThreadTest
    fun test_createBarChartData() {
        val application: Application = ApplicationProvider.getApplicationContext()
        val mockDateRepository = MockDateRepository().apply {
            getWeekDays = listOf(
                "2024/02/04",
                "2024/02/03",
                "2024/02/02",
                "2024/02/01",
                "2024/01/31",
                "2024/01/30",
                "2024/01/29"
            )
        }
        val mockRecordRepository = MockRecordRepository().apply {
            selectCountPerTime = MutableLiveData(mutableListOf(PieChartData("2024/01/29", 0, 0)))
            selectCountPerDay = MutableLiveData(
                mutableListOf(
                    BarChartData("2024/01/29", 0),
                    BarChartData("2024/01/30", 1),
                    BarChartData("2024/01/31", 2),
                    BarChartData("2024/02/01", 3),
                    BarChartData("2024/02/02", 4),
                    BarChartData("2024/02/03", 5),
                    BarChartData("2024/02/04", 6)
                )
            )
        }
        val mockCreateBarChartUseCase = MockCreateBarChartUseCase().apply {
            result = Triple(
                mutableListOf("AAA", "BBB"),
                mutableListOf(BarEntry(1f, 10f), BarEntry(2f, 20f)),
                10
            )
        }

        val graphViewModel = GraphViewModel(
            application,
            mockDateRepository,
            mockRecordRepository,
            mockCreateBarChartUseCase
        )
        graphViewModel.createBarChartData(mockRecordRepository.selectCountPerDay("", "").value!!)
        Assert.assertEquals("2024/01/29 ~ 2024/02/04", graphViewModel.textDuration.value)
        Assert.assertEquals("10回", graphViewModel.textAverageValue.value)
    }
}

class MockDateRepository : DateRepository() {
    var getToday = Calendar.getInstance()
    lateinit var getWeekDays: List<String>

    override fun getToday(): Calendar = getToday

    override fun getAWeekDays(endDate: Calendar): List<String> = getWeekDays
}

class MockRecordRepository : RecordRepository() {
    lateinit var selectCountPerDay: LiveData<MutableList<BarChartData>>
    lateinit var selectCountPerTime: LiveData<MutableList<PieChartData>>

    override fun getSuccessRate(): LiveData<Int> {
        throw NotImplementedError()
    }

    override fun getSuccessCount(): LiveData<Int> {
        throw NotImplementedError()
    }

    override fun getAllSuccess(): LiveData<MutableList<Record>> {
        throw NotImplementedError()
    }

    override fun selectLatest(): LiveData<Record> {
        throw NotImplementedError()
    }

    override fun selectOldest(): LiveData<Record> {
        throw NotImplementedError()
    }

    override fun selectSmallest(): LiveData<Record> {
        throw NotImplementedError()
    }

    override fun selectLargest(): LiveData<Record> {
        throw NotImplementedError()
    }

    override fun selectShortest(): LiveData<Record> {
        throw NotImplementedError()
    }

    override fun selectLongest(): LiveData<Record> {
        throw NotImplementedError()
    }

    override fun selectCountPerDay(
        startDate: String,
        endDate: String
    ): LiveData<MutableList<BarChartData>> = selectCountPerDay

    override fun selectCountPerTime(
        startDate: String,
        endDate: String
    ): LiveData<MutableList<PieChartData>> = selectCountPerTime

    override fun saveRecord(record: Record) {
        throw NotImplementedError()
    }

    override suspend fun saveRecord(record: List<Record>) {
        throw NotImplementedError()
    }

    override suspend fun deleteAll() {
        throw NotImplementedError()
    }
}

class MockCreateBarChartUseCase : CreateBarChartUseCase() {
    lateinit var result: Triple<MutableList<String>, MutableList<BarEntry>, Int>

    override fun invoke(
        barChartData: MutableList<BarChartData>,
        targetDays: List<String>
    ): Triple<MutableList<String>, MutableList<BarEntry>, Int> {
        return result
    }
}
