package com.foodrism.apps.view.profile

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.foodrism.apps.R
import com.foodrism.apps.data.room.HistoryEntity
import com.foodrism.apps.databinding.ActivityProfileBinding
import com.foodrism.apps.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val adapter by lazy { HistoryAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showProfile()
        showHistoryList()
        logOut()

    }

    private fun showHistoryList() {
        val historyList = binding.rvHistoryList
        historyList.setHasFixedSize(true)
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            historyList.layoutManager = GridLayoutManager(this, 2)
        } else {
            historyList.layoutManager = LinearLayoutManager(this)
        }
        val adapter = HistoryAdapter()
        historyList.adapter = adapter


    }


    private fun showProfile() {

    }

    private fun logOut() {

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