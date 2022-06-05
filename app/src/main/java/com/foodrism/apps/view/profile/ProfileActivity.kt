package com.foodrism.apps.view.profile

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.foodrism.apps.R
import com.foodrism.apps.databinding.ActivityProfileBinding
import com.foodrism.apps.view.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        showProfile()
        logOut()

    }

    private fun showProfile() {
        val user = Firebase.auth.currentUser
        user?.let {
            val name = user.displayName
            val photoUrl = user.photoUrl
            val email = user.email

            binding.apply {
                Glide.with(applicationContext)
                    .load(photoUrl)
                    .into(ivProfilePhoto)
                tvProfileName.text = name
                tvProfileEmail.text = email
            }
        }
    }

    private fun logOut() {
        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val button = binding.btnLogout
        val text = SpannableString(getString(R.string.logout))
        text.setSpan(UnderlineSpan(), 0, text.length, 0)
        button.text = text
        button.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.logout))
                setMessage(getString(R.string.logout_message))
                setPositiveButton(getString(R.string.logout_continue)) { _, _ ->
                    auth.signOut()
                    Toast.makeText(
                        this@ProfileActivity,
                        R.string.logout_success,
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }

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