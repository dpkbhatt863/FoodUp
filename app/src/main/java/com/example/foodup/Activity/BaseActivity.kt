package com.example.foodup.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodup.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

open class BaseActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    public val TAG: String = "foodup"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

//        window.statusBarColor = resources.getColor(R.color.white)

    }


}