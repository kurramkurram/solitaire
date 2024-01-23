package io.github.kurramkurram.solitaire.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.data.BarChartData
import io.github.kurramkurram.solitaire.data.PieChartData
import io.github.kurramkurram.solitaire.databinding.FragmentGraphBinding
import io.github.kurramkurram.solitaire.util.CHART_SUCCESS_COUNT_BAR
import io.github.kurramkurram.solitaire.util.CHART_SUCCESS_TIME_PIE
import io.github.kurramkurram.solitaire.util.applyChartStyle
import io.github.kurramkurram.solitaire.viewmodel.GraphViewModel

/**
 * グラフ表示画面.
 */
@AndroidEntryPoint
class GraphFragment : Fragment() {

    private lateinit var binding: FragmentGraphBinding
    private val graphViewModel: GraphViewModel by activityViewModels()

    private val barChartDataObserver = Observer<MutableList<BarChartData>?> {
        it?.let { graphViewModel.createBarChartData(it) }
    }

    private val pieChartDataObserver = Observer<MutableList<PieChartData>?> {
        it?.let { graphViewModel.createPieChartData(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGraphBinding.inflate(layoutInflater, container, false).apply {
            this.viewModel = graphViewModel
            this.lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chartSpinner.apply {
            val array = arrayOf(
                resources.getString(R.string.graph_title_success_count),
                resources.getString(R.string.graph_title_success_time)
            )
            adapter = ArrayAdapter(requireContext(), R.layout.custom_spinner, array).apply {
                setDropDownViewResource(R.layout.custom_spinner_dropdown)
            }
        }
        graphViewModel.spinnerItemLiveData.observe(viewLifecycleOwner) {
            when (it) {
                CHART_SUCCESS_COUNT_BAR -> setSuccessCountBarChart()
                CHART_SUCCESS_TIME_PIE -> setSuccessTimePieChart()
            }
        }
    }

    private fun setSuccessCountBarChart() {
        removeObserver()

        graphViewModel.run {
            barData.observe(viewLifecycleOwner) {
                binding.barChart.apply {
                    applyChartStyle()
                    data = it

                    xAxis.apply {
                        isEnabled = true
                        position = XAxis.XAxisPosition.BOTTOM
                        textColor = Color.WHITE
                        setDrawGridLines(false)
                        valueFormatter = IndexAxisValueFormatter(graphViewModel.labelList)
                        setDrawLabels(true)
                    }

                    axisLeft.apply {
                        textColor = Color.WHITE
                        axisLineColor = Color.WHITE
                        gridColor = Color.WHITE
                        spaceBottom = 0.1f
                        axisMinimum = 0f
                    }

                    axisRight.isEnabled = false

                    invalidate()
                }
            }
            barChartData.observe(viewLifecycleOwner, barChartDataObserver)
        }
    }

    private fun setSuccessTimePieChart() {
        removeObserver()

        graphViewModel.run {
            pieData.observe(viewLifecycleOwner) {
                binding.pieChart.apply {
                    applyChartStyle()
                    data = it
                    invalidate()
                }
            }
            pieChartData.observe(viewLifecycleOwner, pieChartDataObserver)
        }
    }

    private fun removeObserver() {
        graphViewModel.run {
            barChartData.removeObserver(barChartDataObserver)
            pieChartData.removeObserver(pieChartDataObserver)
        }
    }

    companion object {
        private const val TAG = "GraphFragment"
    }
}
