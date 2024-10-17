package com.example.foodup.Domain

import java.io.Serializable

class Foods : Serializable {

    var CategoryId: Int = 0
    var Description: String? = null
    var BestFood: Boolean = false
    var Id: Int = 0
    var LocationId: Int = 0
    var Price: Double = 0.0
    var ImagePath: String? = null
    var PriceId: Int = 0
    var Star: Double = 0.0
    var TimeId: Int = 0
    var TimeValue: Int = 0
    var Title: String? = null
    var numberInCart: Int = 0

    override fun toString(): String {
        return "$CategoryId, $Description, $BestFood, $Id, $LocationId, " +
                "$Price, $ImagePath, $PriceId, $Star, $TimeId, " +
                "$TimeValue, $Title"
    }
}