package fr.group5.magellangpt.data.remote

import fr.group5.magellangpt.data.remote.dto.down.ConversationDtoDown
import fr.group5.magellangpt.data.remote.dto.down.MessageDtoDown
import fr.group5.magellangpt.data.remote.dto.down.ModelDtoDown
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming
import java.util.UUID

interface ApiService {

//    @POST("conversations/{id}/messages")
//    suspend fun postMessage(@Path("id") id : UUID, @Body message : String) : List<String?>

    @POST("conversations/{id}/messages")
    suspend fun postMessage(@Path("id") id : Int, @Body message : String) : String

    @POST("conversations/messages")
    suspend fun postMessageToNewConversation(@Body message : String) : List<String?>

    @GET("conversations/{id}/messages")
    suspend fun getConversationMessages(@Path("id") id : UUID) : List<MessageDtoDown>

    @GET("models")
    suspend fun getModels() : List<ModelDtoDown>

    @GET("conversations")
    suspend fun getConversations() : List<ConversationDtoDown>
}