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
import com.example.foodup.Activity.DetailActivity
import com.example.foodup.Domain.Foods
import com.example.foodup.R

class BestFoodsAdapter(private val foodList: List<Foods>,private val context: Context) : RecyclerView.Adapter<BestFoodsAdapter.FoodViewHolder>() {

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTxt = itemView.findViewById<TextView>(R.id.titleTxt)
        val priceTxt = itemView.findViewById<TextView>(R.id.priceTxt)
        val starTxt = itemView.findViewById<TextView>(R.id.starTxt)
        val pic = itemView.findViewById<ImageView>(R.id.pic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_best_foods, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = foodList[position]
        // Bind data to views here
        holder.titleTxt.text = foodItem.Title
        holder.priceTxt.text = "₹${foodItem.Price}"
        holder.starTxt.text = "${foodItem.Star}"

        // Assuming foodItem.imagePath is a URL or file path
        Glide.with(context)
            .load(foodItem.ImagePath)
            .transform(CenterCrop())
            .into(holder.pic)

        holder.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent: Intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("foods",foodItem)
                context.startActivity(intent)
            }

        })
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}