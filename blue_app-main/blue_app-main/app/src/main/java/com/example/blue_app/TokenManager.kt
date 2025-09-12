package com.example.blue_app

import android.content.Context

fun saveToken(context: Context, token: String) {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString("auth_token", token).apply()
}

fun getToken(context: Context): String? {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return prefs.getString("auth_token", null)
}

fun saveUserId(context: Context, userId: Int) {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("user_id", userId).apply()
}

fun getUserId(context: Context): Int {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return prefs.getInt("user_id", 0)
}
