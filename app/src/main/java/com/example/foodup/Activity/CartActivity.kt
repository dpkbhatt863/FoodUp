package com.example.foodup.Activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodup.Adapter.CartAdapter
import com.example.foodup.Helper.ManagmentCart
import com.example.foodup.R
import com.example.foodup.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var managmentCart: ManagmentCart
    private lateinit var adapter: RecyclerView.Adapter<*>

    private var tax: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

        managmentCart = ManagmentCart(this)
        setVariable()
        calculateCart()
        initList()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.visibility = View.VISIBLE
            binding.scrollViewCart.visibility = View.GONE
        } else {
            binding.emptyTxt.visibility = View.GONE
            binding.scrollViewCart.visibility = View.VISIBLE
        }

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.cardView.layoutManager= LinearLayoutManager(this)
        adapter = CartAdapter(managmentCart.getListCart(), this) { calculateCart() }
        binding.cardView.adapter = adapter

    }


    private fun calculateCart() {
        val percentTax = 0.02 // 2% tax
        val delivery = 100.0 // 100 rupee delivery fee
        tax = Math.round(managmentCart.getTotalFee() * percentTax * 100.0) / 100.0
        val total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100.0) / 100.0
        val itemTotal = Math.round(managmentCart.getTotalFee() * 100.0) / 100.0

        binding.totalFeeText.text = "₹$itemTotal"
        binding.taxTxt.text = "₹$tax"
        binding.deliveryTxt.text = "₹$delivery"
        binding.totalTxt.text = "₹$total"

    }

    private fun setVariable() {

    }
}