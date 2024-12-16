package com.example.artur_leonel_pe_modulo_3

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UsuarioApi {
    @POST("api/Usuarios")
    fun cadastrarUsuario(@Body usuario: Usuario): Call<Void>

    @POST("/api/Usuarios/Login")
    fun realizarLogin(
        @Query("email") email: String,
        @Query("senha") senha: String
    ): Call<LoginResponse>

    // Método para obter detalhes do usuário pelo ID
    @GET("/api/Usuarios/{id}") // Ajuste o endpoint conforme necessário
    fun obterDetalhesUsuario(@Path("id") id: String): Call<UserDetailsResponse>

    // Método para listar todos os usuários
    @GET("/api/Usuarios")
    fun listarUsuarios(): Call<List<UserDetailsResponse>>

}
