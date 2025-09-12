package com.example.blue_app

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val password_confirmation: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val success: Boolean,
    val data: AuthData?,
    val message: String?
)

data class AuthData(
    val user: UserData,
    val token: String,
    val token_type: String
)

@Parcelize
data class UserData(
    val id: Int,
    val name: String,
    val email: String
) : Parcelable

interface AuthService {
    @POST("register")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>

    @POST("login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>
}


