package fr.group5.magellangpt.domain.repositories

import fr.group5.magellangpt.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getMessages() : Flow<List<Message>>

    suspend fun sendMessage(content: String)
}