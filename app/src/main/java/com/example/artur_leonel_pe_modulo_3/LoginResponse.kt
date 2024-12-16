package com.example.artur_leonel_pe_modulo_3

data class LoginResponse(
    val token: String,
    val email: String,
    val dr: String,
    val id: Int,
)