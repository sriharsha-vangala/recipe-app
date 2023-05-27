package com.example.recipeapplication.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExtendedIngredient(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("unit")
    val unit: String
): Parcelable