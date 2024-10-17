package com.example.foodup.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.foodup.Activity.DetailActivity
import com.example.foodup.Domain.Foods
import com.example.foodup.R

class FoodListAdapter(private val foodList: List<Foods>,private val context: Context) : RecyclerView.Adapter<FoodListAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_list_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = foodList[position]

        holder.titleTxt.text = foodItem.Title
        holder.priceTxt.text = "₹${foodItem.Price}"
        holder.starTxt.text = "${foodItem.Star}"

        Glide.with(context)
                .load(foodItem.ImagePath)
                .transform(CenterCrop())
                .into(holder.pict)

        holder.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent(context,DetailActivity::class.java)
                intent.putExtra("foods",foodItem)
                context.startActivity(intent)
            }

        })

    }

    override fun getItemCount(): Int = foodList.size

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt = itemView.findViewById<TextView>(R.id.titleTxt)
        val priceTxt = itemView.findViewById<TextView>(R.id.priceTxt)
        val starTxt = itemView.findViewById<TextView>(R.id.rateTxt)
        val pict = itemView.findViewById<ImageView>(R.id.img)

    }
}
