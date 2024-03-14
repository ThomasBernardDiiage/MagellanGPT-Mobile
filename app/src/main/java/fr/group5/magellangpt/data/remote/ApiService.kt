package fr.group5.magellangpt.data.remote

import fr.group5.magellangpt.data.remote.dto.down.ConversationDtoDown
import fr.group5.magellangpt.data.remote.dto.down.ModelDtoDown
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming

interface ApiService {

    @POST("message")
    suspend fun sendMessage(@Body message : String) : List<String?>

    @GET("models")
    suspend fun getModels() : List<ModelDtoDown>

    @POST("message")
    suspend fun getConversations() : List<ConversationDtoDown>
}