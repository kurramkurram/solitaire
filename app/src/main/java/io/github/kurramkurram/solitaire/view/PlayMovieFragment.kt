package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.kurramkurram.solitaire.databinding.FragmentPlayMovieBinding
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
            movieItems.observe(viewLifecycleOwner) {
                movieAdapter.submitList(it)
            }
            navigation.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let {
                    val dialog = DialogPlayMovieFragment.newInstance(this.selectFile!!.path)
                    dialog.show(requireActivity().supportFragmentManager, SHOW_DIALOG_KEY)
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d(TAG, "#onHiddenChanged hidden = $hidden")
        if (!hidden) {
            playMovieViewModel.updateMovie()
        }
    }

    companion object {
        const val TAG = "PlayMovieFragment"
    }
}