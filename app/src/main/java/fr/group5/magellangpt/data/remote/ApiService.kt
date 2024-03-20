package fr.group5.magellangpt.data.remote

import fr.group5.magellangpt.data.remote.dto.down.ConversationDtoDown
import fr.group5.magellangpt.data.remote.dto.down.ConversationListDtoDown
import fr.group5.magellangpt.data.remote.dto.down.ConversationMessageDtoDown
import fr.group5.magellangpt.data.remote.dto.down.ModelDtoDown
import fr.group5.magellangpt.data.remote.dto.up.MessageDtoUp
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.UUID

interface ApiService {

//    @POST("conversations/{id}/messages")
//    suspend fun postMessage(@Path("id") id : UUID, @Body message : String) : List<String?>


    @GET("conversations")
    suspend fun getConversations() : List<ConversationListDtoDown>


    @GET("conversations/{id}")
    suspend fun getConversation(@Path("id") id : UUID) : ConversationDtoDown

    @Multipart
    @POST("conversations/{id}/messages")
    suspend fun postMessage(@Part("id") id : UUID, @Part("message") message : MessageDtoUp, @Part files: List<MultipartBody.Part>) : String

    @POST("conversations/messages")
    suspend fun postMessageToNewConversation(@Body message : String) : List<String?>

    @GET("models")
    suspend fun getModels() : List<ModelDtoDown>


}