package io.github.kurramkurram.solitaire.view

import android.app.ActionBar
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.data.TrumpCard
import io.github.kurramkurram.solitaire.databinding.SolitaireItemBinding
import io.github.kurramkurram.solitaire.util.L
import io.github.kurramkurram.solitaire.util.SIDE
import io.github.kurramkurram.solitaire.viewmodel.SolitaireViewModel
import kotlinx.coroutines.NonDisposableHandle.parent

private object DiffCallback : DiffUtil.ItemCallback<TrumpCard>() {
    override fun areItemsTheSame(oldItem: TrumpCard, newItem: TrumpCard): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TrumpCard, newItem: TrumpCard): Boolean =
        oldItem == newItem
}

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

                item.side.observe(viewLifecycleOwner) {
                    val context = binding.root.context
                    val color = if (item.side.value == SIDE.BACK) {
                        android.R.color.darker_gray
                    } else if (item.pattern.ordinal % 2 == 0) {
                        R.color.white
                    } else {
                        android.R.color.holo_red_light
                    }
                    itemView.setBackgroundColor(context.getColor(color))
                }

                val height = if (viewModel.isLast(item)) {
                    200
                } else {
                    50
                }
                L.d(TAG, "#height = $height")
                LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, height).apply {
                    itemView.layoutParams = this
                }

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