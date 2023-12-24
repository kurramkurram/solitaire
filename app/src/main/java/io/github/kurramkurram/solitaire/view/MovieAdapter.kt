package io.github.kurramkurram.solitaire.view

import android.annotation.SuppressLint
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.kurramkurram.solitaire.databinding.PreviewViewBinding
import io.github.kurramkurram.solitaire.util.DATE_PATTERN_HH_MM
import io.github.kurramkurram.solitaire.viewmodel.PlayMovieViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

private object MovieDiffCallback : DiffUtil.ItemCallback<File>() {
    override fun areItemsTheSame(oldItem: File, newItem: File): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: File, newItem: File): Boolean =
        oldItem == newItem
}

class MovieAdapter(
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewModel: PlayMovieViewModel
) : ListAdapter<File, MovieAdapter.MovieViewHolder>(MovieDiffCallback) {

    class MovieViewHolder(private val binding: PreviewViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(
            item: File,
            viewLifecycleOwner: LifecycleOwner,
            viewModel: PlayMovieViewModel
        ) {
            binding.run {
                lifecycleOwner = viewLifecycleOwner
                this.viewModel = viewModel
                Date()
                val date =
                    SimpleDateFormat(DATE_PATTERN_HH_MM).format(Date(item.name.split(".")[0].toLong()))
                fileName = item.name
                this.date = date

                val retriever = MediaMetadataRetriever()
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