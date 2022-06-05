package com.foodrism.apps.view.scan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.foodrism.apps.R
import com.foodrism.apps.databinding.ActivityScanBinding
import com.foodrism.apps.helper.rotateBitmap
import com.foodrism.apps.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var getFile: File? = null
    private var isBackCamera = false

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

        getFile = intent.getSerializableExtra(EXTRA_IMAGE) as File
        scanResult()

    }


    private fun scanResult() {
        binding.analyzeProgressBar.visibility = View.VISIBLE
        isBackCamera = intent.getBooleanExtra(EXTRA_STATE, true)

        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            val imageView =
                rotateBitmap(BitmapFactory.decodeFile((getFile as File).path), isBackCamera)
            val result = analyzeModel()
            handler.post {
                binding.tvAnalyzeResult.text = result
                binding.ivPreviewImage.setImageBitmap(imageView)
                binding.analyzeProgressBar.visibility = View.GONE
            }
        }

    }

    private fun analyzeModel(): String {
        // Parsing the label from assets/label.txt
        val fileName = "label.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        val foodLabel = inputString.split("\n")

        // Create scaled input for model
        val model = Model.newInstance(this)
        val bitmap = BitmapFactory.decodeFile(getFile?.path)
        val input: Bitmap = Bitmap.createScaledBitmap(bitmap, 448, 448, true)

        val tfImage = TensorImage.fromBitmap(input)
        val byteBuffer = tfImage.buffer

        // Input for reference
        val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 448, 448, 3), DataType.UINT8)
        inputFeature.loadBuffer(byteBuffer)

        // Runs model inference
        val outputs = model.process(inputFeature)
        val outputFeature = outputs.outputFeature0AsTensorBuffer
        val max = getMax(outputFeature.floatArray)

        // Output
        val modelResult = foodLabel[max]
        model.close()

        return modelResult
    }

    private fun getMax(array: FloatArray): Int {
        var index = 0
        var min = 0.0f
        for (i in 0..14) {
            if (array[i] > min) {
                index = i
                min = array[i]
            }
        }
        return index
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

