package io.github.kurramkurram.solitaire.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionConfig
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.kurramkurram.solitaire.databinding.FragmentSolitaireBinding
import io.github.kurramkurram.solitaire.service.RecordService
import io.github.kurramkurram.solitaire.util.*
import io.github.kurramkurram.solitaire.viewmodel.SolitaireViewModel
import kotlinx.android.synthetic.main.fragment_solitaire.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SolitaireFragment : Fragment() {

    private val solitaireViewModel: SolitaireViewModel by activityViewModels()

    private lateinit var layoutList: MutableList<RecyclerView>
    private val listAdapterList: MutableList<CardAdapter> = mutableListOf()

    private val startRecordPermission = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = Intent(requireContext(), RecordService::class.java)
            intent.putExtra(RECORD_RESULT_DATA, result.data)
            requireContext().startForegroundService(intent)
            solitaireViewModel.recording.value = true
        }
    }

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
            val fragment = DialogRestartFragment()
            fragment.show(parentFragmentManager, SHOW_DIALOG_KEY)
            solitaireViewModel.stopTimer()
        }

        setFragmentResultListener(DIALOG_RESULT_RESTART) { _, data ->
            val result = data.getInt(DIALOG_RESULT_KEY, -1)
            if (result == DIALOG_RESULT_OK) {
                solitaireViewModel.onRestartClick()
                initLayout()
            } else {
                solitaireViewModel.restartTimer()
            }
        }

        auto_complete.setOnClickListener {
            val ret = runBlocking {
                getPreference(requireContext(), NO_MORE_CHECKBOX_KEY, false).first()
            }

            L.d(TAG, "#onViewCreated ret = $ret")
            if (ret) {
                solitaireViewModel.startAutoCompleteAsync()
            } else {
                val fragment = DialogAutoCompleteFragment()
                fragment.show(parentFragmentManager, SHOW_DIALOG_KEY)
                solitaireViewModel.stopTimer()
            }
        }

        setFragmentResultListener(DIALOG_RESULT_AUTO_COMPLETE) { _, data ->
            val result = data.getInt(DIALOG_RESULT_KEY, -1)
            solitaireViewModel.restartTimer()
            if (result == DIALOG_RESULT_OK) {
                solitaireViewModel.startAutoCompleteAsync()
            }
        }

        recording_button.setOnClickListener {
            if (solitaireViewModel.recording.value!!) {
                val intent = Intent(requireContext(), RecordService::class.java)
                requireContext().stopService(intent)
                solitaireViewModel.recording.value = false
            } else {
                val projectionManager =
                    requireContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    startRecordPermission.launch(
                        projectionManager.createScreenCaptureIntent(
                            MediaProjectionConfig.createConfigForUserChoice()
                        )
                    )
                } else {
                    startRecordPermission.launch(projectionManager.createScreenCaptureIntent())
                }
            }
        }
    }

    private fun initLayout() {
        listAdapterList.clear()
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

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "#onPause ")

        val intent = Intent(requireContext(), RecordService::class.java)
        requireContext().stopService(intent)
    }

    companion object {
        const val TAG = "SolitaireFragment"
    }
}