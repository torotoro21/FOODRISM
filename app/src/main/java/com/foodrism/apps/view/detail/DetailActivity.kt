package com.foodrism.apps.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.foodrism.apps.R
import com.foodrism.apps.databinding.ActivityDetailBinding
import com.foodrism.apps.model.FoodModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_DETAIL = "food"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupData()
    }

    private fun setupData() {
        val food = intent.getParcelableExtra<FoodModel>(EXTRA_DETAIL) as FoodModel
        binding.apply {
            Glide.with(applicationContext)
                .load(food.url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivFoodItems)
            tvFoodName.text = food.name
            tvFoodDescription.text = food.description
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}