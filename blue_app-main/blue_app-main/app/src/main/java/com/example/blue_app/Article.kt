package com.example.blue_app

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import com.example.blue_app.UserData

@Parcelize
data class Article(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    @SerializedName("images") val images: List<ArticleImage> = emptyList(),
    @SerializedName("user_id") val user_id: Int, // Nuevo campo
    @SerializedName("user") val user: UserData? = null // Mantener como opcional
) : Parcelable

@Parcelize
data class ArticleImage(
    val id: Int,
    val url: String
) : Parcelable


