package fr.group5.magellangpt.domain.repositories

import fr.group5.magellangpt.domain.models.Message
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

interface MessageRepository {
    fun getMessages() : Flow<List<Message>>

    @Throws(HttpException::class)
    suspend fun sendMessage(content: String)
}