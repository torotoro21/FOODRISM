package com.foodrism.apps.view.profile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.foodrism.apps.data.room.HistoryEntity
import com.foodrism.apps.databinding.LayoutItemBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var historyList = emptyList<HistoryEntity>()
    private var onItemClicked: OnItemClickCallback? = null

    inner class ViewHolder(private val binding: LayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryEntity) {
            binding.root.setOnClickListener { onItemClicked?.onItemClicked(history) }
            binding.apply {
                Glide.with(itemView)
                    .load(history.image)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivFoodItems)
                tvFoodName.text = history.name
                tvFoodDescription.text = history.date
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size


    @SuppressLint("NotifyDataSetChanged")
    fun setData(history: List<HistoryEntity>) {
        this.historyList = history
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(history: HistoryEntity)
    }
}