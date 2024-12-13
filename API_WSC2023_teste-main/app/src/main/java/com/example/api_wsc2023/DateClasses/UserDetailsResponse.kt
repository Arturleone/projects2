package com.example.api_wsc2023.DateClasses

data class UserDetailsResponse(
    val id: String,
    val email: String?,
    val dr: String?,
    val nome: String? // Adicione este campo se não estiver presente
)

