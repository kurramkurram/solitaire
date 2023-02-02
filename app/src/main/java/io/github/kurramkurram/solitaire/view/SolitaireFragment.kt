package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.kurramkurram.solitaire.databinding.FragmentSolitaireBinding
import io.github.kurramkurram.solitaire.util.L
import io.github.kurramkurram.solitaire.viewmodel.SolitaireViewModel
import kotlinx.android.synthetic.main.fragment_solitaire.*

class SolitaireFragment : Fragment() {

    private val solitaireViewModel by viewModels<SolitaireViewModel>()

    private lateinit var layoutList: MutableList<RecyclerView>
    private val listAdapterList: MutableList<CardAdapter> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSolitaireBinding.inflate(inflater, container, false).apply {
            this.viewModel = solitaireViewModel
            this.lifecycleOwner = viewLifecycleOwner
        }.run { root }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        L.d(TAG, "#onViewCreated")

        layoutList = mutableListOf(
            listView0,
            listView1,
            listView2,
            listView3,
            listView4,
            listView5,
            listView6
        )
        initLayout()

        restart_button.setOnClickListener {
            solitaireViewModel.onRestartClick()
            listAdapterList.clear()
            initLayout()
        }
    }

    private fun initLayout() {
        for (layout in layoutList) {
            layout.run {
                layoutManager = LinearLayoutManager(context)
                adapter = CardAdapter(
                    viewLifecycleOwner,
                    this@SolitaireFragment.solitaireViewModel
                ).also {
                    listAdapterList.add(it)
                }
            }
        }

        solitaireViewModel.run {
            listLayout.observe(viewLifecycleOwner) {
                for ((index, adapter) in listAdapterList.withIndex()) {
                    adapter.submitList(ArrayList(it[index]))
                }
            }
        }
    }

    companion object {
        private const val TAG = "SolitaireFragment"
    }
}