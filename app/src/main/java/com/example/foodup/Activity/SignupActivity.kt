package com.example.foodup.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodup.R
import com.example.foodup.databinding.ActivitySignupBinding

class SignupActivity : BaseActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //inflater
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setVariable();
    }

    private fun setVariable(){
        binding.signupBtn.setOnClickListener {
            val email = binding.userEdt.text.toString()
            val password = binding.passEdt.text.toString()

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.i(TAG,"onComplete")
                    startActivity(Intent(this,MainActivity::class.java))
                } else {
                    Log.i(TAG,"Failure"+ task.exception)
                    Toast.makeText(this,"Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}