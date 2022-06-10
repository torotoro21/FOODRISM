package com.foodrism.apps.view.scan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.foodrism.apps.R
import com.foodrism.apps.databinding.ActivityScanBinding
import com.foodrism.apps.helper.rotateBitmap
import com.foodrism.apps.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var getFile: File? = null
    private var isBackCamera = false

    companion object {
        const val EXTRA_IMAGE = "photo"
        const val EXTRA_STATE = "isBackCamera"
        private const val TAG = "ScanActivity"
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
        // Create scaled image input
        val bitmap = BitmapFactory.decodeFile((getFile as File).path)
        val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true) as Bitmap

        val input = ByteBuffer
            .allocateDirect(224 * 224 * 3 * 4)
            .order(ByteOrder.nativeOrder())
        for (y in 0 until 224) {
            for (x in 0 until 224) {
                val px = resized.getPixel(x, y)
                val r = Color.red(px)
                val g = Color.green(px)
                val b = Color.blue(px)

                val rf = (r - 127) / 255f
                val gf = (g - 127) / 255f
                val bf = (b - 127) / 255f

                input.putFloat(rf)
                input.putFloat(gf)
                input.putFloat(bf)
            }
        }
        val model = Model.newInstance(this)

        // Input for reference
        val inputFeature0 = TensorBuffer
            .createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(input)

        // Runs model inference
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        for (i in 0..14) {
            val value = outputFeature0.floatArray[i]
            Log.i(TAG, "analyzeModel: accuracy index $i = $value")
        }

        // Output - Parsing the label from assets/label.txt
        val fileName = "label.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        val foodLabel = inputString.split("\n")

        // Get probability index
        val max = getMax(outputFeature0.floatArray)

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