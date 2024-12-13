package com.example.apiapplicationtest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @GET("users")
    fun getUsers(): Call<List<User>>

    @POST("users")
    fun createUser(@Body user: User): Call<User>
}
