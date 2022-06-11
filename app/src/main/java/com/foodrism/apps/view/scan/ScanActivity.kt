package com.foodrism.apps.view.scan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.foodrism.apps.R
import com.foodrism.apps.databinding.ActivityScanBinding
import com.foodrism.apps.helper.compressImage
import com.foodrism.apps.helper.rotateBitmap
import com.foodrism.apps.ml.Model
import com.foodrism.apps.model.FoodModel
import com.foodrism.apps.view.camera.CameraActivity
import com.foodrism.apps.view.detail.DetailActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private lateinit var bitmap: Bitmap
    private var imageFile: File? = null
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

        imageFile = intent.getSerializableExtra(EXTRA_IMAGE) as File
        val compressed = compressImage(imageFile as File)
        bitmap = BitmapFactory.decodeFile((compressed).path)

        scanResult()
        analyzeModel()

        binding.btnCamera.setOnClickListener {
            val intent = Intent(this@ScanActivity, CameraActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun scanResult() {
        binding.analyzeProgressBar.visibility = View.VISIBLE
        isBackCamera = intent.getBooleanExtra(EXTRA_STATE, true)

        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            val imageView = rotateBitmap(bitmap, isBackCamera)
            handler.post {
                binding.ivPreviewImage.setImageBitmap(imageView)
                binding.analyzeProgressBar.visibility = View.GONE
            }
        }
    }

    private fun analyzeModel() {
        // Create scaled image input
        val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true) as Bitmap

        // Input for reference
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
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(input)


        // Runs model inference
        val model = Model.newInstance(this)
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val highestProbabilityIndex = getMax(outputFeature0.floatArray)

        for (i in 0..14) {
            val value = outputFeature0.floatArray[i]
            Log.i(TAG, "analyzeModel: accuracy index $i = $value")
        }
        val result = showResult(highestProbabilityIndex, outputFeature0.floatArray)

        model.close()

        return result
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

    @SuppressLint("SetTextI18n")
    private fun showResult(index: Int, array: FloatArray) {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.getStringArray(R.array.data_photo_url)
        val dataOrigin = resources.getStringArray(R.array.data_origin)
        val dataIngredients = resources.getStringArray(R.array.data_ingredients)
        val dataCalories = resources.getStringArray(R.array.data_calories)
        val dataNutrition = resources.getStringArray(R.array.data_nutrition)

        val listFoodData = ArrayList<FoodModel>()
        val fileName = "label.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        val foodLabel = inputString.split("\n")

        var min = 0.0f
        for (i in 0..14) {
            if (array[i] > min) {
                min = array[i]
            }
        }
        val percentage = min * 100

        for (i in foodLabel.indices) {
            val food = FoodModel(
                dataName[i],dataDescription[i],dataOrigin[i],dataPhoto[i],dataIngredients[i],dataCalories[i],dataNutrition[i]
            )
            listFoodData.add(food)
        }

        val data = listFoodData[index]
        binding.apply {
            Glide.with(applicationContext)
                .load(data.url)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivFoodItems)
            tvFoodName.text = data.name
            tvFoodDescription.text = data.description
            tvAccuracy.text = "(${percentage}%)"
        }
        binding.cvItemResult.setOnClickListener {
            val intent = Intent(it.context, DetailActivity::class.java)
            val transition: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    it.context as Activity,
                    Pair(binding.ivFoodItems, "cardStory")
                )
            intent.putExtra(DetailActivity.EXTRA_DETAIL, data)
            it.context.startActivity(intent, transition.toBundle())
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