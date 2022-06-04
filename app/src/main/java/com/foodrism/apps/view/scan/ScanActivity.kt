package com.foodrism.apps.view.scan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.foodrism.apps.R
import com.foodrism.apps.databinding.ActivityScanBinding
import com.foodrism.apps.helper.AppExecutors
import com.foodrism.apps.helper.compressImage
import com.foodrism.apps.helper.rotateBitmap
import java.io.File
import java.io.FileOutputStream

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var getFile: File? = null
    private var isBackCamera = false
    private val appExecutor: AppExecutors by lazy {
        AppExecutors()
    }

    companion object {
        const val EXTRA_IMAGE = "photo"
        const val EXTRA_STATE = "isBackCamera"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setTitle(R.string.scan_result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        previewImage()
    }

    private fun previewImage() {
        getFile = intent.getSerializableExtra(EXTRA_IMAGE) as File
        isBackCamera = intent.getBooleanExtra(EXTRA_STATE, true)

        val result = rotateBitmap(BitmapFactory.decodeFile((getFile as File).path), isBackCamera)
        result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(getFile))

        appExecutor.diskIO.execute {
            getFile = compressImage(getFile as File)
        }
        binding.ivPreviewImage.setImageBitmap(result)
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

