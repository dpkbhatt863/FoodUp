package com.example.foodup.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodup.Adapter.CategoryAdapter
import com.example.foodup.Adapter.FoodListAdapter
import com.example.foodup.Domain.Category
import com.example.foodup.Domain.Foods
import com.example.foodup.Domain.Location
import com.example.foodup.Domain.Price
import com.example.foodup.R
import com.example.foodup.databinding.ActivityListFoodsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class ListFoodsActivity : BaseActivity() {

    private lateinit var binding: ActivityListFoodsBinding
    private lateinit var adapterListFood: RecyclerView.Adapter<*>

    private var CategoryId: Int = 0
    private var categoryName: String? = null
    private var searchText: String? = null
    private var isSearch: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListFoodsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getIntentExtra()
        initList()
        setVariable()
    }

    private fun setVariable() {

    }

    private fun initList() {
        val database = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Foods")
        binding.progressBar.visibility = View.VISIBLE
        val list = ArrayList<Foods>()

        val query: Query
        if (isSearch) {
            query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText + '\uf8ff')
        } else {
            Log.d("ListFoodsActivity", "Executing query with Category ID: $CategoryId")
            query = myRef.orderByChild("CategoryId").equalTo(CategoryId.toDouble())
        }

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        snap.getValue(Foods::class.java)?.let {
                            list.add(it)
                        }
                    }
                    if (list.isNotEmpty()) {
                        binding.foodListView.layoutManager = GridLayoutManager(this@ListFoodsActivity, 2)
                        adapterListFood = FoodListAdapter(list, this@ListFoodsActivity)
                        binding.foodListView.adapter = adapterListFood
                    }
                }
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Error loading data", error.toException())
            }
        })
    }

    private fun getIntentExtra() {
        CategoryId = intent.getIntExtra("CategoryId", 0)
        categoryName = intent.getStringExtra("CategoryName")
        searchText = intent.getStringExtra("text")
        isSearch = intent.getBooleanExtra("isSearch", false)

        Log.d("ListFoodsActivity", "Category ID: $CategoryId")
        Log.d("ListFoodsActivity", "Category Name: $categoryName")
        Log.d("ListFoodsActivity", "Search Text: $searchText")
        Log.d("ListFoodsActivity", "Is Search: $isSearch")

        binding.titleTxt.text = categoryName
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}
