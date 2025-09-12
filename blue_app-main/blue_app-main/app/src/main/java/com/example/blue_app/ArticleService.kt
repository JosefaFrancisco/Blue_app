package com.example.blue_app

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ArticleService {
    @GET("articles")
    fun getArticles(): Call<ArticleResponse>

    @Multipart
    @POST("articles")
    fun createArticle(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Call<Article>

    @PUT("articles/{id}")
    fun updateArticle(
        @Path("id") id: Int,
        @Body article: HashMap<String, Any>
    ): Call<Article>

    @Multipart
    @POST("articles/{id}/images")
    fun uploadImages(
        @Path("id") id: Int,
        @Part images: List<MultipartBody.Part>
    ): Call<Article>

    @DELETE("articles/{id}")
    fun deleteArticle(@Path("id") id: Int): Call<Void>
}