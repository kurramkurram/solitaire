package io.github.kurramkurram.solitaire.view

import android.annotation.SuppressLint
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.kurramkurram.solitaire.data.Movie
import io.github.kurramkurram.solitaire.databinding.PreviewViewBinding
import io.github.kurramkurram.solitaire.util.DATE_PATTERN_HH_MM
import io.github.kurramkurram.solitaire.util.L
import io.github.kurramkurram.solitaire.viewmodel.PlayMovieViewModel
import java.text.SimpleDateFormat
import java.util.Date

private object MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.fileName == newItem.fileName

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem
}

/**
 * 動画のリストアダプター.
 */
class MovieAdapter(
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewModel: PlayMovieViewModel
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback) {

    class MovieViewHolder(private val binding: PreviewViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(
            item: Movie,
            viewLifecycleOwner: LifecycleOwner,
            viewModel: PlayMovieViewModel
        ) {
            binding.run {
                lifecycleOwner = viewLifecycleOwner
                this.viewModel = viewModel
                val date =
                    SimpleDateFormat(DATE_PATTERN_HH_MM).format(Date(item.fileName.split(".")[0].toLong()))
                fileName = item.fileName
                this.date = date

                val retriever = MediaMetadataRetriever()
                L.d(TAG, "#bind path = ${item.path}")
                retriever.setDataSource(item.path)
                val durationString: String? =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                durationString?.let {
                    val bitmap = retriever.getFrameAtTime(
                        it.toLong() * 1000,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                    )
                    capture = bitmap
                    executePendingBindings()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(
            PreviewViewBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position), viewLifecycleOwner, viewModel)
    }

    companion object {
        private const val TAG = "MovieAdapter"
    }
}
