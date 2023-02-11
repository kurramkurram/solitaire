package io.github.kurramkurram.solitaire.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.kurramkurram.solitaire.data.Record
import io.github.kurramkurram.solitaire.databinding.RecordItemBinding
import io.github.kurramkurram.solitaire.viewmodel.RecordViewModel

private object RecordDiffCallback : DiffUtil.ItemCallback<Record>() {
    override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean = oldItem == newItem
}

class RecordListAdapter(
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewModel: RecordViewModel
) : ListAdapter<Record, RecordListAdapter.RecordViewHolder>(RecordDiffCallback) {

    class RecordViewHolder(private val binding: RecordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Record,
            position: Int,
            viewLifecycleOwner: LifecycleOwner,
            viewModel: RecordViewModel
        ) {
            binding.run {
                lifecycleOwner = viewLifecycleOwner
                record = item
                rank = position + 1
                this.viewModel = viewModel

                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RecordViewHolder(RecordItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bind(getItem(position), position, viewLifecycleOwner, viewModel)
    }
}