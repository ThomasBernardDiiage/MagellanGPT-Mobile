package fr.group5.magellangpt.data.remote

import fr.group5.magellangpt.data.remote.dto.down.ConversationDtoDown
import fr.group5.magellangpt.data.remote.dto.down.ConversationListDtoDown
import fr.group5.magellangpt.data.remote.dto.down.MessageDtoDown
import fr.group5.magellangpt.data.remote.dto.down.ModelDtoDown
import fr.group5.magellangpt.data.remote.dto.down.TcuAnalysisDtoDown
import fr.group5.magellangpt.data.remote.dto.down.TcuQuestionDtoDown
import fr.group5.magellangpt.data.remote.dto.up.CreateConversationDtoUp
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.UUID

interface ApiService {
    @GET("conversations")
    suspend fun getConversations() : List<ConversationListDtoDown>

    @POST("conversations")
    suspend fun createConversation(@Body body : CreateConversationDtoUp) : ConversationDtoDown

    @GET("conversations/{id}")
    suspend fun getConversation(@Path("id") id : UUID) : ConversationDtoDown

    @Multipart
    @POST("conversations/{id}/messages")
    suspend fun postMessage(
        @Path("id") id : UUID,
        @Part model : MultipartBody.Part,
        @Part message : MultipartBody.Part,
        @Part files: List<MultipartBody.Part>) : Response<MessageDtoDown>

    @GET("models")
    suspend fun getModels() : List<ModelDtoDown>

    @Multipart
    @POST("documents")
    suspend fun uploadDocument(@Part document : MultipartBody.Part)


    @GET("tcu/questions")
    suspend fun getTcuQuestions() : List<TcuQuestionDtoDown>

    @Multipart
    @POST("tcu/questions/answers")
    suspend fun tcuAnalysis(@Part document : MultipartBody.Part) : List<TcuAnalysisDtoDown>
}