package fr.group5.magellangpt.domain.repositories

import fr.group5.magellangpt.domain.models.Conversation
import fr.group5.magellangpt.domain.models.Message
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.net.UnknownHostException

interface ConversationRepository {
    @Throws(HttpException::class, UnknownHostException::class)
    suspend fun getConversations() : List<Conversation>

    @Throws(HttpException::class, UnknownHostException::class)
    fun getMessages() : Flow<List<Message>>

    @Throws(HttpException::class, UnknownHostException::class)
    suspend fun sendMessage(content: String)
}