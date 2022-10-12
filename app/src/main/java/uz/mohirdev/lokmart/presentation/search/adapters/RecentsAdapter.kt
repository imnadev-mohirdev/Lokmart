package uz.mohirdev.lokmart.presentation.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.mohirdev.lokmart.databinding.ItemRecentBinding

class RecentsAdapter(
    private val recents: List<String>,
    private val onClick: (search: String) -> Unit
) : RecyclerView.Adapter<RecentsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRecentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(search: String) {
            binding.search.text = search
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recents[position])
    }

    override fun getItemCount() = recents.size
}