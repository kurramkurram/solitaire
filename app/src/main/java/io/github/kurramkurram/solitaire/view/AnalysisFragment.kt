package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.kurramkurram.solitaire.databinding.FragmentAnalysisBinding
import io.github.kurramkurram.solitaire.viewmodel.AnalysisViewModel

/**
 * 統計画面.
 */
@AndroidEntryPoint
class AnalysisFragment : Fragment() {

    private val analysisViewModel: AnalysisViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAnalysisBinding.inflate(inflater, container, false).apply {
        this.viewModel = analysisViewModel
        this.lifecycleOwner = viewLifecycleOwner
    }.run { root }

    companion object {
        const val TAG = "AnalysisFragment"
    }
}
