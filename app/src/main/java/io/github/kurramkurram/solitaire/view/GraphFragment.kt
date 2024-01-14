package io.github.kurramkurram.solitaire.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.databinding.FragmentGraphBinding
import io.github.kurramkurram.solitaire.viewmodel.GraphViewModel

/**
 * グラフ表示画面.
 */
class GraphFragment : Fragment() {

    private lateinit var binding: FragmentGraphBinding
    private val graphViewModel: GraphViewModel by activityViewModels()

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

    override fun onResume() {
        super.onResume()

        binding.chartSpinner.apply {
            val array = arrayOf(resources.getString(R.string.graph_title_success))
            adapter = ArrayAdapter(requireContext(), R.layout.custom_spinner, array).apply {
                setDropDownViewResource(R.layout.custom_spinner_dropdown)
            }

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, p3: Long) {
                    if (pos == 0) {
                        setSuccessBarChart()
                    } else {
                        binding.barChart.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun setSuccessBarChart() {
        graphViewModel.run {
            barData.observe(viewLifecycleOwner) {
                binding.barChart.apply {
                    visibility = View.VISIBLE
                    legend.textColor = Color.WHITE
                    description.isEnabled = false
                    animateY(1000)

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

                    setTouchEnabled(false)
                    invalidate()
                }
            }
            barChartData.observe(viewLifecycleOwner) {
                graphViewModel.createBarChartData(it)
            }
        }
    }

    companion object {
        private const val TAG = "GraphFragment"
    }
}
