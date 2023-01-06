package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.util.L
import io.github.kurramkurram.solitaire.viewmodel.SolitaireViewModel
import kotlinx.android.synthetic.main.fragment_solitaire.*

class SolitaireFragment : Fragment() {

    private val solitaireViewModel by viewModels<SolitaireViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        solitaireViewModel.initCard()
        return inflater.inflate(R.layout.fragment_solitaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        L.d(TAG, "#onViewCreated")

        listView0.adapter = CardListAdapter(requireContext(), solitaireViewModel.layoutList[0])
        listView1.adapter = CardListAdapter(requireContext(), solitaireViewModel.layoutList[1])
        listView2.adapter = CardListAdapter(requireContext(), solitaireViewModel.layoutList[2])
        listView3.adapter = CardListAdapter(requireContext(), solitaireViewModel.layoutList[3])
        listView4.adapter = CardListAdapter(requireContext(), solitaireViewModel.layoutList[4])
        listView5.adapter = CardListAdapter(requireContext(), solitaireViewModel.layoutList[5])
        listView6.adapter = CardListAdapter(requireContext(), solitaireViewModel.layoutList[6])
    }

    companion object {
        private const val TAG = "SolitaireFragment"
    }
}