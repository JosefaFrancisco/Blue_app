package com.example.blue_app

object Validators {
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
        return emailRegex.matches(email)
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
}