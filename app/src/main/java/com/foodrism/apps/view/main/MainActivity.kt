package com.foodrism.apps.view.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodrism.apps.R
import com.foodrism.apps.databinding.ActivityMainBinding
import com.foodrism.apps.model.FoodModel
import com.foodrism.apps.view.adapter.FoodListAdapter
import com.foodrism.apps.view.camera.CameraActivity
import com.foodrism.apps.view.profile.ProfileActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<FoodModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCamera.setOnClickListener {
            val intent = Intent(this@MainActivity, CameraActivity::class.java)
            startActivity(intent)
        }

        list.addAll(getData)
        showRecyclerList()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuProfile) {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerList() {
        val foodList = binding.rvFoodList
        foodList.setHasFixedSize(true)
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            foodList.layoutManager = GridLayoutManager(this, 2)
        } else {
            foodList.layoutManager = LinearLayoutManager(this)
        }
        val adapter = FoodListAdapter(list)
        foodList.adapter = adapter
    }

    private val getData: ArrayList<FoodModel>
        get() {
            val dataName = resources.getStringArray(R.array.data_name)
            val dataDescription = resources.getStringArray(R.array.data_description)
            val dataPhoto = resources.getStringArray(R.array.data_photo_url)
            val dataOrigin  = resources.getStringArray(R.array.data_origin)
            val listFoodData = ArrayList<FoodModel>()
            for (i in dataName.indices){
                val food  = FoodModel(dataName[i],  dataDescription[i], dataOrigin[i], dataPhoto[i])
                listFoodData.add(food)
            }
            return listFoodData
        }

}