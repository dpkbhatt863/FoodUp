package com.example.foodup.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.foodup.Domain.Foods
import com.example.foodup.Helper.ChangeNumberItemsListener
import com.example.foodup.Helper.ManagmentCart
import com.example.foodup.R

class CartAdapter(
    private var list: ArrayList<Foods>,
    context: Context,

    private val changeNumberItemsListener: ChangeNumberItemsListener
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private val managementCart: ManagmentCart = ManagmentCart(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_cart, parent, false)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodItem = list[position]
        holder.title.text = foodItem.Title
        holder.feeEachItem.text = "₹${foodItem.Price}"
        holder.totalEachItem.text = "₹${foodItem.numberInCart * foodItem.Price}"
        holder.num.text = foodItem.numberInCart.toString()

        Glide.with(holder.itemView.context)
            .load(foodItem.ImagePath)
            .transform(CenterCrop(), RoundedCorners(30))
            .into(holder.pic)

        holder.plusItem.setOnClickListener {
            managementCart.plusNumberItem(list, position, object : ChangeNumberItemsListener {
                override fun change() {
                    notifyDataSetChanged()
                    changeNumberItemsListener.change()
                }
            })
        }

        holder.minusItem.setOnClickListener {
            managementCart.minusNumberItem(list, position, object : ChangeNumberItemsListener {
                override fun change() {
                    notifyDataSetChanged()
                    changeNumberItemsListener.change()
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleTxt)
        val pic: ImageView = itemView.findViewById(R.id.pic)
        val feeEachItem: TextView = itemView.findViewById(R.id.feeEachitem)
        val plusItem: TextView = itemView.findViewById(R.id.plusCartBtn)
        val minusItem: TextView = itemView.findViewById(R.id.minusCartBtn)
        val totalEachItem: TextView = itemView.findViewById(R.id.totalEachItem)
        val num: TextView = itemView.findViewById(R.id.numberItemTxt)
    }
}
