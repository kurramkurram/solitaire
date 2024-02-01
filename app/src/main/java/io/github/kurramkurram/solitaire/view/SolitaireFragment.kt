package io.github.kurramkurram.solitaire.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionConfig
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.kurramkurram.solitaire.databinding.FragmentSolitaireBinding
import io.github.kurramkurram.solitaire.service.RecordService
import io.github.kurramkurram.solitaire.util.APP_CONFIRM
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_APP_CONFIRM
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_AUTO_COMPLETE
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_KEY
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_OK
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_RESTART
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_START_MOVIE
import io.github.kurramkurram.solitaire.util.getPreference
import io.github.kurramkurram.solitaire.util.L
import io.github.kurramkurram.solitaire.util.NO_MORE_CHECKBOX_KEY
import io.github.kurramkurram.solitaire.util.NO_MORE_CHECKBOX_MOVIE_KEY
import io.github.kurramkurram.solitaire.util.RECORD_RESULT_DATA
import io.github.kurramkurram.solitaire.util.SHOW_DIALOG_KEY
import io.github.kurramkurram.solitaire.viewmodel.SolitaireViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * ゲーム画面.
 */
class SolitaireFragment : Fragment() {

    private val solitaireViewModel: SolitaireViewModel by activityViewModels()
    private lateinit var binding: FragmentSolitaireBinding

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
        binding = FragmentSolitaireBinding.inflate(inflater, container, false).apply {
            this.viewModel = solitaireViewModel
            this.lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        L.d(TAG, "#onViewCreated")

        binding.apply {
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

            restartButton.setOnClickListener {
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

            autoComplete.setOnClickListener {
                val ret = runBlocking {
                    getPreference(requireContext(), NO_MORE_CHECKBOX_KEY, false).first()
                }

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

            recordingButton.setOnClickListener {
                if (solitaireViewModel.recording.value!!) {
                    val intent = Intent(requireContext(), RecordService::class.java)
                    requireContext().stopService(intent)
                    solitaireViewModel.recording.value = false
                } else {
                    val ret = runBlocking {
                        getPreference(requireContext(), NO_MORE_CHECKBOX_MOVIE_KEY, false).first()
                    }
                    if (ret) {
                        startMovie()
                    } else {
                        val fragment = DialogStartMovieFragment()
                        fragment.show(parentFragmentManager, SHOW_DIALOG_KEY)
                    }
                }
            }

            setFragmentResultListener(DIALOG_RESULT_START_MOVIE) { _, data ->
                val result = data.getInt(DIALOG_RESULT_KEY, -1)
                if (result == DIALOG_RESULT_OK) {
                    solitaireViewModel.onStartMovieDialogPositiveClicked()
                    startMovie()
                }
            }

            val isAppConfirm = runBlocking {
                getPreference(requireContext(), APP_CONFIRM, false).first()
            }
            if (!isAppConfirm) {
                val fragment = DialogAppFragment()
                fragment.show(parentFragmentManager, SHOW_DIALOG_KEY)
            }
            setFragmentResultListener(DIALOG_RESULT_APP_CONFIRM) { _, data ->
                val result = data.getInt(DIALOG_RESULT_KEY, -1)
                if (result == DIALOG_RESULT_OK) {
                    solitaireViewModel.onSaveAppConfirm()
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

    private fun startMovie() {
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

    override fun onPause() {
        super.onPause()
        L.d(TAG, "#onPause ")
        val intent = Intent(requireContext(), RecordService::class.java)
        requireContext().stopService(intent)
        solitaireViewModel.recording.value = false
    }

    companion object {
        const val TAG = "SolitaireFragment"
    }
}
