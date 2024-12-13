package com.example.modulo03

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://apieuvounatrip.azurewebsites.net/"

    val usuarioApi: UsuarioApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuarioApi::class.java)
    }
}