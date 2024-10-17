package com.example.foodup.Helper

import android.content.Context
import android.widget.Toast
import com.example.foodup.Domain.Foods

import java.util.ArrayList

class ManagmentCart(private val context: Context) {

    private val tinyDB = TinyDB(context)

    fun insertFood(item: Foods) {
        val listpop = getListCart()
        val existingItemIndex = listpop.indexOfFirst { it.Title == item.Title }

        if (existingItemIndex != -1) {
            listpop[existingItemIndex].numberInCart = item.numberInCart
        } else {
            listpop.add(item)
        }

        tinyDB.putListObject("CartList", listpop)
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): ArrayList<Foods> {
        return tinyDB.getListObject("CartList")
    }

    fun getTotalFee(): Double {
        return getListCart().sumOf { it.Price * it.numberInCart }
    }

    fun minusNumberItem(listItem: ArrayList<Foods>, position: Int, changeNumberItemsListener: ChangeNumberItemsListener) {
        if (listItem[position].numberInCart == 1) {
            listItem.removeAt(position)
        } else {
            listItem[position].numberInCart -= 1
        }
        tinyDB.putListObject("CartList", listItem)
        changeNumberItemsListener.change()
    }

    fun plusNumberItem(listItem: ArrayList<Foods>, position: Int, changeNumberItemsListener: ChangeNumberItemsListener) {
        listItem[position].numberInCart += 1
        tinyDB.putListObject("CartList", listItem)
        changeNumberItemsListener.change()
    }
}
