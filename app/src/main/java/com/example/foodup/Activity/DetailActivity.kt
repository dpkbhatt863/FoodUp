package com.example.foodup.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.foodup.Domain.Foods
import com.example.foodup.Helper.ManagmentCart
import com.example.foodup.R
import com.example.foodup.databinding.ActivityDetailBinding
import java.io.Serializable

class DetailActivity : AppCompatActivity() {

    private val TAG = "DetailActivity"

    private lateinit var binding : ActivityDetailBinding
    private lateinit var foods : Foods
    private lateinit var managementCart : ManagmentCart
    var num : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.black)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d(TAG, "onCreate: Activity started") // Log activity start

        getIntentExtra()
        setVariable()
    }

    private fun setVariable() {
        managementCart = ManagmentCart(this);
        if (!::foods.isInitialized) {
            Log.e(TAG, "setVariable: Foods data is not initialized")
            finish() // or show an error message to the user
            return
        }
        binding.backBtn.setOnClickListener { finish() }


        try {
            Glide.with(this)
                .load(foods.ImagePath)
                .into(binding.pic)

            binding.priceTxt.text = "₹" + foods.Price
            binding.titleTxt.text = foods.Title
            binding.descriptionTxt.text = foods.Description
            binding.rateTxt.text = foods.Star.toString() + " Rating"
            binding.ratingBar.rating = foods.Star.toFloat()
            binding.totalTxt.text = "${num * foods.Price} ₹"

            binding.plusBtn.setOnClickListener {
                num += 1
                binding.numTxt.text = (num + 1).toString()
                binding.totalTxt.text = "+${num * foods.Price}"
            }

            binding.minusBtn.setOnClickListener {
                num -= 1
                binding.numTxt.text = (num + 1).toString()
                binding.totalTxt.text = "+${num * foods.Price}"
            }

            binding.addBtn.setOnClickListener {
                foods.numberInCart=num
                managementCart.insertFood(foods)
            }




            Log.d(TAG, "setVariable: Variables set successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error in setVariable: ${e.localizedMessage}", e)
        }



    }

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    private fun getIntentExtra() {

        foods = intent.serializable("foods") ?: return

    }
}