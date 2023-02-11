package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.kurramkurram.solitaire.databinding.FragmentRecordBinding
import io.github.kurramkurram.solitaire.viewmodel.RecordViewModel
import kotlinx.android.synthetic.main.fragment_record.*

class RecordFragment : Fragment() {

    private val recordViewModel: RecordViewModel by activityViewModels()
    private lateinit var recordListAdapter: RecordListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return FragmentRecordBinding.inflate(inflater, container, false).apply {
            this.viewModel = recordViewModel
            this.lifecycleOwner = viewLifecycleOwner
        }.run { root }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        record_list.run {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            adapter = RecordListAdapter(
                viewLifecycleOwner,
                this@RecordFragment.recordViewModel
            ).also {
                recordListAdapter = it
            }
        }

        recordViewModel.run {
            recordList.observe(viewLifecycleOwner) {
                recordListAdapter.submitList(it)
            }
        }
    }

    companion object {
        const val TAG = "RecordFragment"
    }
}