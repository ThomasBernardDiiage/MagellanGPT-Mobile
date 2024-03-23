package fr.group5.magellangpt.domain.repositories

import android.net.Uri
import fr.group5.magellangpt.domain.models.Conversation
import fr.group5.magellangpt.domain.models.Message
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.net.UnknownHostException
import java.util.UUID

interface ConversationRepository {
    /**
     * Get the list of all conversations
     */
    @Throws(HttpException::class, UnknownHostException::class)
    suspend fun getConversations() : List<Conversation>

    /**
     * Get the list of all conversations, the result will be emitted in a flow of getMessages
     */
    @Throws(HttpException::class, UnknownHostException::class)
    suspend fun getMessages(conversationId : UUID)

    /**
     * Get the list of all messages
     */
    @Throws(HttpException::class, UnknownHostException::class)
    fun getMessages() : Flow<List<Message>>

    /**
     * Send a message to a conversation. Files can be attached
     */
    @Throws(HttpException::class, UnknownHostException::class)
    suspend fun sendMessage(conversationId : UUID, content: String, uris : List<Uri>)

    /**
     * Create a new conversation, preprompt is optional
     */
    @Throws(HttpException::class, UnknownHostException::class)
    suspend fun createConversation(name: String, prePrompt: String) : Conversation
}