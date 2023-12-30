package io.github.kurramkurram.solitaire.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.kurramkurram.solitaire.data.TrumpCard
import io.github.kurramkurram.solitaire.databinding.SolitaireItemBinding
import io.github.kurramkurram.solitaire.viewmodel.SolitaireViewModel

private object DiffCallback : DiffUtil.ItemCallback<TrumpCard>() {
    override fun areItemsTheSame(oldItem: TrumpCard, newItem: TrumpCard): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TrumpCard, newItem: TrumpCard): Boolean =
        oldItem == newItem
}

/**
 * 場札のカードアダプター.
 */
class CardAdapter(
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewModel: SolitaireViewModel
) : ListAdapter<TrumpCard, CardAdapter.TrumpViewHolder>(DiffCallback) {

    class TrumpViewHolder(private val binding: SolitaireItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: TrumpCard,
            viewLifecycleOwner: LifecycleOwner,
            viewModel: SolitaireViewModel
        ) {
            binding.run {
                lifecycleOwner = viewLifecycleOwner
                trumpCard = item
                this.viewModel = viewModel

                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrumpViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TrumpViewHolder(SolitaireItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: TrumpViewHolder, position: Int) {
        holder.bind(getItem(position), viewLifecycleOwner, viewModel)
    }

    companion object {
        private const val TAG = "CardAdapter"
    }
}
