package com.example.foodup.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodup.R
import com.example.foodup.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {

    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //inflater
        binding = ActivityIntroBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setVariable();
        window.statusBarColor = Color.parseColor("#F65564")

    }

    private fun setVariable(){
        binding.loginBtn.setOnClickListener{
            try {
                if (mAuth.currentUser != null) {
                    Log.d("IntroActivity", "User is logged in. Navigating to MainActivity.")
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Log.d("IntroActivity", "No user logged in. Navigating to LoginActivity.")
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            } catch (e: Exception) {
                Log.e("IntroActivity", "Error on login button click", e)
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("An error occurred: ${e.message}")
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }
        }

        binding.signupBtn.setOnClickListener{
            startActivity(Intent(this,SignupActivity::class.java))
        }
    }
}

