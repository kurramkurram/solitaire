package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.databinding.FragmentPlayMovieBinding
import io.github.kurramkurram.solitaire.util.*
import io.github.kurramkurram.solitaire.util.SHOW_DIALOG_KEY
import io.github.kurramkurram.solitaire.viewmodel.PlayMovieViewModel
import kotlinx.android.synthetic.main.fragment_play_movie.*

class PlayMovieFragment : Fragment() {

    private val playMovieViewModel: PlayMovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPlayMovieBinding.inflate(inflater, container, false).apply {
        this.viewModel = playMovieViewModel
        this.lifecycleOwner = viewLifecycleOwner
    }.run { root }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieAdapter = MovieAdapter(
            viewLifecycleOwner,
            this@PlayMovieFragment.playMovieViewModel
        )
        movie_recycler_view.apply {
            layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
            adapter = movieAdapter
        }
        playMovieViewModel.run {
            movieInfo.observe(viewLifecycleOwner) {
                movieAdapter.submitList(it)
                empty_text.visibility = if (it.size > 0) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }

            navigation.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    when (it) {
                        CLICKED_MOVIE_ITEM -> {
                            val dialog = DialogPlayMovieFragment.newInstance(selectFilePath)
                            dialog.show(requireActivity().supportFragmentManager, SHOW_DIALOG_KEY)
                        }

                        CLICKED_RESET_BUTTON -> {
                            val dialog = DialogResetFragment.newInstance(
                                requireContext().resources.getString(R.string.reset_movie_dialog_title),
                                requireContext().resources.getString(R.string.reset_movie_dialog_text)
                            )
                            dialog.show(requireActivity().supportFragmentManager, SHOW_DIALOG_KEY)
                            setFragmentResultListener(DIALOG_RESULT_RESET) { _, data ->
                                val result = data.getInt(DIALOG_RESULT_KEY, -1)
                                if (result == DIALOG_RESULT_OK) {
                                    deleteAll()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "PlayMovieFragment"
    }
}