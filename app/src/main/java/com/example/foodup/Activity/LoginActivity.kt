package com.example.foodup.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodup.R
import com.example.foodup.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setVariable()


    }

    private fun setVariable() {
        binding.loginBtn.setOnClickListener {
            val email :String = binding.userEdt.text.toString()
            val password :String = binding.passEdt.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) { task->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(this, "Please fill username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}