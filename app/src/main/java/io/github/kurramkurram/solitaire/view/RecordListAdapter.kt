package io.github.kurramkurram.solitaire.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import io.github.kurramkurram.solitaire.data.Record
import io.github.kurramkurram.solitaire.databinding.RecordItemBinding
import io.github.kurramkurram.solitaire.viewmodel.RecordViewModel

/**
 * 成功記録のカードアダプター.
 */
class RecordListAdapter(
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewModel: RecordViewModel
) : RecyclerView.Adapter<RecordListAdapter.RecordViewHolder>() {

    private val list: MutableList<Record> = mutableListOf()

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
        holder.bind(list[position], position, viewLifecycleOwner, viewModel)
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<Record>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }
}
