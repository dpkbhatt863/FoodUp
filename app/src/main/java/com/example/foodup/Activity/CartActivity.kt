package com.example.foodup.Activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodup.Adapter.CartAdapter
import com.example.foodup.Helper.ChangeNumberItemsListener
import com.example.foodup.Helper.ManagmentCart
import com.example.foodup.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartAdapter
    private lateinit var managementCart: ManagmentCart
    private var tax = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagmentCart(this)

        setVariable()
        calculateCart()
        initList()
    }

    private fun initList() {
        if (managementCart.getListCart().isEmpty()) {
            binding.emptyTxt.visibility = View.VISIBLE
            binding.scrollViewCart.visibility = View.GONE
        } else {
            binding.emptyTxt.visibility = View.GONE
            binding.scrollViewCart.visibility = View.VISIBLE

            val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.cardView.layoutManager = linearLayoutManager
            adapter = CartAdapter(managementCart.getListCart(), this, object :
                ChangeNumberItemsListener {
                override fun change() {
                    calculateCart()  // Calls calculateCart() when items are changed
                }
            })
            binding.cardView.adapter = adapter
        }
    }

    private fun calculateCart() {
        val percentTax = 8.82 // percent tax
        val delivery = 50.0 // delivery fee
        val totalFee = managementCart.getTotalFee()

        tax = Math.round(totalFee * percentTax / 100.0 * 100) / 100.0
        val total = Math.round((totalFee + tax + delivery) * 100) / 100.0
        val itemTotal = Math.round(totalFee * 100) / 100.0

        binding.totalFeeText.text = "₹"+itemTotal
        binding.taxTxt.text = "₹"+tax
        binding.deliveryTxt.text = "₹"+delivery
        binding.totalTxt.text = "₹"+total
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}
