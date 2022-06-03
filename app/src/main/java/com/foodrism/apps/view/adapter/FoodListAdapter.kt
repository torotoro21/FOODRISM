package com.foodrism.apps.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foodrism.apps.model.FoodModel

class FoodListAdapter(private val foodList: ArrayList<FoodModel>) :
    RecyclerView.Adapter<FoodListAdapter.ListViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        TODO("Not yet implemented")
    }


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {



    }
}