package com.example.modulo03

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.http.Path

interface UsuarioApi{
    @POST("api/Usuarios")
    fun cadastrarUsuario(@Body usuario: Usuario): Call<Void>

    @POST("/api/Usuarios/Login")
    fun realizarLogin(
        @Query("email") email: String,
        @Query("senha") senha: String): Call<LoginResponse>

    @GET("/api/Usuarios/{id}") // Ajuste o endpoint conforme necessário
    fun obterDetalhesUsuario(@Path("id") id: String): Call<UserDetailsResponse>

    @GET("/api/Usuarios")
    fun listarUsuarios(): Call<List<UserDetailsResponse>>
}
