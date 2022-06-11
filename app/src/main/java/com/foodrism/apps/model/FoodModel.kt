package com.foodrism.apps.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodModel(
    var name: String,
    var description: String,
    var origin: String,
    var url: String,
    var ingredients: String,
    var calories: String,
    var nutrition: String
) : Parcelable