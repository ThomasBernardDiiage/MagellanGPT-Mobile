package fr.group5.magellangpt.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("message")
    suspend fun sendMessage(@Body message : String): String
}