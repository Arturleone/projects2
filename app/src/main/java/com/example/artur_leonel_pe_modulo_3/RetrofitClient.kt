package com.example.artur_leonel_pe_modulo_3

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://apieuvounatrip.azurewebsites.net/"

    val usuarioApi: UsuarioApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(UsuarioApi::class.java)
    }
}
