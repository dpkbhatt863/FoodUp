package com.example.foodup.Adapter

//import android.view.RoundedCorner
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.foodup.Activity.ListFoodsActivity
import com.example.foodup.Domain.Category
import com.example.foodup.Domain.Foods
import com.example.foodup.R
import com.google.firebase.database.ValueEventListener

class CategoryAdapter(private val items: List<Category>, private val context: Context) : RecyclerView.Adapter<CategoryAdapter.FoodViewHolder>() {

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTxt = itemView.findViewById<TextView>(R.id.catNameText)
        val pic = itemView.findViewById<ImageView>(R.id.imgCat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_category, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = items[position]
        // Bind data to views here
        holder.titleTxt.text = foodItem.Name

        when(position){
            0 -> holder.pic.setBackgroundResource(R.drawable.cat_1_bg)
            1 -> holder.pic.setBackgroundResource(R.drawable.cat_2_bg)
            2 -> holder.pic.setBackgroundResource(R.drawable.cat_3_bg)
            3 -> holder.pic.setBackgroundResource(R.drawable.cat_4_bg)
            4 -> holder.pic.setBackgroundResource(R.drawable.cat_5_bg)
            5 -> holder.pic.setBackgroundResource(R.drawable.cat_6_bg)
            6 -> holder.pic.setBackgroundResource(R.drawable.cat_7_bg)
            7 -> holder.pic.setBackgroundResource(R.drawable.cat_8_bg)
        }

        val drawableResourceId = context.resources.getIdentifier(foodItem.ImagePath,"drawable",context.packageName)

        Glide.with(context)
            .load(drawableResourceId)
            .into(holder.pic)

        holder.itemView.setOnClickListener {
            val intent: Intent = Intent(context,ListFoodsActivity::class.java)
            intent.putExtra("CategoryId", foodItem.Id)
            intent.putExtra("CategoryName",foodItem.Name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}