package com.foodrism.apps.view.scan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.foodrism.apps.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding

    companion object {
        const val EXTRA_IMAGE = "photo"
        const val EXTRA_STATE = "state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}