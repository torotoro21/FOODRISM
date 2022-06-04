package com.foodrism.apps.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.foodrism.apps.databinding.LayoutItemBinding
import com.foodrism.apps.model.FoodModel
import com.foodrism.apps.view.detail.DetailActivity
import androidx.core.util.Pair
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class FoodListAdapter(private val foodList: ArrayList<FoodModel>) :
    RecyclerView.Adapter<FoodListAdapter.ListViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = LayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(foodList[position])
    }

    override fun getItemCount() = foodList.size

    inner class ListViewHolder(private var binding: LayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: FoodModel) {
            binding.root.setOnClickListener {
                val intent = Intent(it.context, DetailActivity::class.java)
                val transition: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        it.context as Activity,
                        Pair(binding.ivFoodItems, "cardStory")
                    )
                intent.putExtra(DetailActivity.EXTRA_DETAIL, food)
                it.context.startActivity(intent, transition.toBundle())
            }
            binding.apply {
                Glide.with(itemView)
                    .load(food.url)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivFoodItems)
                tvFoodName.text = food.name
                tvFoodDescription.text = food.description
            }
        }


    }
}