package com.example.modulo03

data class LoginResponse(
    val token: String,
    val email: String,
    val dr: String,
    val id: Int,
)
