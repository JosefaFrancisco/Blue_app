package com.example.blue_app

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "http://10.0.13.196:8000/api/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
                .addHeader("Accept", "application/json")

            // 🚀 Añadimos token automáticamente si existe
            val token = getToken(App.instance)
            if (!token.isNullOrEmpty()) {
                builder.addHeader("Authorization", "Bearer $token")
            }

            // Solo agregamos Content-Type si no es multipart
            val contentType = original.body?.contentType()?.subtype
            if (contentType != "form-data") {
                builder.addHeader("Content-Type", "application/json")
            }

            chain.proceed(builder.build())
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val articleService: ArticleService by lazy {
        retrofit.create(ArticleService::class.java)
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

}
