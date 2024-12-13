package com.example.api_wsc2023.DateClasses

data class LoginResponse(
    val token: String,
    val email: String, // Você pode remover se não precisar dele aqui
    val dr: String,    // Você pode remover se não precisar dele aqui
    val id: Int       // Adicionando o campo ID
)
