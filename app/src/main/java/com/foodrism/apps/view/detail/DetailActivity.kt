package com.foodrism.apps.view.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.foodrism.apps.R
import com.foodrism.apps.databinding.ActivityDetailBinding
import com.foodrism.apps.model.FoodModel

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var foodName: TextView
    private lateinit var food_name: String

    companion object {
        const val EXTRA_DETAIL = "food"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupData()

        foodName = findViewById(R.id.tvFoodName)
        val maps: Button = findViewById(R.id.btnMaps)
        maps.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnMaps -> {
                food_name = foodName.text.toString()
                val gmmIntentUri = Uri.parse("geo:0,0?p=${food_name}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
            }
        }
    }

    private fun setupData() {
        val food = intent.getParcelableExtra<FoodModel>(EXTRA_DETAIL) as FoodModel
        supportActionBar?.title = food.name
        binding.apply {
            Glide.with(applicationContext)
                .load(food.url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivFoodItems)
            tvFoodName.text = food.name
            tvFoodDescription.text = food.description
            tvFoodIngredients.text = food.ingredients
            tvFoodCalories.text = food.calories
            tvFoodNutrition.text = food.nutrition

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