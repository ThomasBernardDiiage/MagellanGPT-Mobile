package fr.group5.magellangpt.domain.repositories

import android.net.Uri
import fr.group5.magellangpt.domain.models.Conversation
import fr.group5.magellangpt.domain.models.Message
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.net.UnknownHostException
import java.util.UUID

interface ConversationRepository {
    @Throws(HttpException::class, UnknownHostException::class)
    suspend fun getConversations() : List<Conversation>

    @Throws(HttpException::class, UnknownHostException::class)
    suspend fun getMessages(conversationId : UUID)

    @Throws(HttpException::class, UnknownHostException::class)
    fun getMessages() : Flow<List<Message>>

    @Throws(HttpException::class, UnknownHostException::class)
    suspend fun sendMessage(conversationId : UUID, content: String, uris : List<Uri>)

    @Throws(HttpException::class, UnknownHostException::class)
    suspend fun sendMessage(content: String)
}