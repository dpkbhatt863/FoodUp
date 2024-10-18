package com.example.foodup.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodup.Adapter.BestFoodsAdapter
import com.example.foodup.Adapter.CategoryAdapter
import com.example.foodup.Domain.Category
import com.example.foodup.Domain.Foods
import com.example.foodup.Domain.Location
import com.example.foodup.Domain.Price
import com.example.foodup.Domain.Time
import com.example.foodup.R
import com.example.foodup.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mAuth.currentUser == null) {
                    // If user is logged out, go to the intro page
                    val intent = Intent(this@MainActivity, IntroActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    // Handle normal back press
                    finish()
                }
            }
        })

        initLocation()
        initTime()
        initPrice()
        initBestFood()
        initCategory()
        setVariable()
    }

    private fun setVariable() {
        binding.logoutBtn.setOnClickListener {
            mAuth.signOut()
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Clear the back stack and navigate to the login/intro activity
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.searchBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val text: String = binding.searchEdt.text.toString()
                if (text.isNotEmpty()){
                    val intent :Intent = Intent(this@MainActivity,ListFoodsActivity::class.java)
                    intent.putExtra("text",text)
                    intent.putExtra("isSearch",true)
                    startActivity(intent)
                }
            }

        })

        binding.cartBtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent(this@MainActivity, CartActivity::class.java)
                startActivity(intent)
            }

        })
    }


    private fun initCategory() {
        val database = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("Category")
        binding.progressBarCategory.visibility = View.VISIBLE
        val list = ArrayList<Category>()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        snap.getValue(Category::class.java)?.let {
                            list.add(it)
                            Log.d("FoodImagePath", "Image path: ${it.ImagePath}")
                        }
                    }
                    Log.d("BestFoodsAdapter", "Number of items: ${list.size}")
                    if (list.isNotEmpty()) {
                        binding.categoryView.layoutManager = GridLayoutManager(this@MainActivity,4)
                        val adapter = CategoryAdapter(list, this@MainActivity)
                        binding.categoryView.adapter = adapter
                    }
                    binding.progressBarCategory.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun initBestFood() {
        val database = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("Foods")
        binding.progressBarBestFood.visibility = View.VISIBLE
        val list = ArrayList<Foods>()

        val query : Query = myRef.orderByChild("BestFood").equalTo(true)

        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        snap.getValue(Foods::class.java)?.let {
                            list.add(it)
                            Log.d("FoodImagePath", "Image path: ${it.ImagePath}")
                        }
                    }
                    Log.d("BestFoodsAdapter", "Number of items: ${list.size}")
                    if (list.isNotEmpty()) {
                        binding.bestFoodView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                        val adapter = BestFoodsAdapter(list, this@MainActivity)
                        binding.bestFoodView.adapter = adapter
                    }
                    binding.progressBarBestFood.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun initLocation() {
        val database = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("Location")
        val list = ArrayList<Location>()

        myRef.addListenerForSingleValueEvent( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        snap.getValue(Location::class.java)?.let { list.add(it) }
                    }
                    // Ensure Location has a meaningful toString() implementation or create a custom adapter
                    val adapter = ArrayAdapter(this@MainActivity, R.layout.sp_item, list.map { it.toString() })
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.locationSp.adapter = adapter
                } else {
                    Log.e("MainActivity", "Snapshot does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Database error: ${error.message}")

            }

        }

        )

    }

    private fun initTime() {
        val database = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("Time")
        val list = ArrayList<Time>()

        myRef.addListenerForSingleValueEvent( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        snap.getValue(Time::class.java)?.let { list.add(it) }
                    }
                    // Ensure Location has a meaningful toString() implementation or create a custom adapter
                    val adapter = ArrayAdapter(this@MainActivity, R.layout.sp_item, list.map { it.toString() })
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.timeSp.adapter = adapter
                } else {
                    Log.e("MainActivity", "Snapshot does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Database error: ${error.message}")

            }

        }

        )

    }

    private fun initPrice() {
        val database = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("Price")
        val list = ArrayList<Price>()

        myRef.addListenerForSingleValueEvent( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        snap.getValue(Price::class.java)?.let { list.add(it) }
                    }
                    // Ensure Location has a meaningful toString() implementation or create a custom adapter
                    val adapter = ArrayAdapter(this@MainActivity, R.layout.sp_item, list.map { it.toString() })
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.priceSp.adapter = adapter
                } else {
                    Log.e("MainActivity", "Snapshot does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Database error: ${error.message}")

            }

        }

        )

    }
}