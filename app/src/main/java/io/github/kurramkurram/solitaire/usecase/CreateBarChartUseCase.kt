package io.github.kurramkurram.solitaire.usecase

import com.github.mikephil.charting.data.BarEntry
import io.github.kurramkurram.solitaire.data.BarChartData

/**
 * 成功数の棒グラフを作成するユースケース.
 */
abstract class CreateBarChartUseCase {
    abstract fun invoke(
        barChartData: MutableList<BarChartData>,
        targetDays: List<String>
    ): Triple<MutableList<String>, MutableList<BarEntry>, Int>
}

class CreateBarChartUseCaseImpl : CreateBarChartUseCase() {

    override fun invoke(
        barChartData: MutableList<BarChartData>,
        targetDays: List<String>
    ): Triple<MutableList<String>, MutableList<BarEntry>, Int> {
        if (barChartData.size != targetDays.size) {
            targetDays.forEach { date ->
                var isContain = false
                barChartData.forEach BarChartLoop@{ barChartData ->
                    if (date == barChartData.day) {
                        isContain = true
                        return@BarChartLoop
                    }
                }
                if (!isContain) {
                    barChartData.add(BarChartData(date, 0))
                }
            }
            barChartData.sortBy { it.day }
        }
        val entryList = mutableListOf<BarEntry>()
        val labelList = mutableListOf<String>()
        var sum = 0
        barChartData.forEachIndexed { index, barChartData ->
            entryList.add(BarEntry(index.toFloat(), barChartData.count.toFloat()))
            labelList.add(barChartData.day.substring(5, 10))
            sum += barChartData.count
        }

        return Triple(labelList, entryList, sum / targetDays.size)
    }
}
