package com.foodrism.apps.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.foodrism.apps.databinding.ActivityLoginBinding
import com.foodrism.apps.view.camera.CameraActivity
import com.foodrism.apps.view.main.MainActivity
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

//        binding.signInButton.setOnClickListener { loginSetup() }

        binding.loginButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, CameraActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginSetup() {

    }


    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            binding.progressBar.visibility = View.GONE
            finish()
        }
    }

}