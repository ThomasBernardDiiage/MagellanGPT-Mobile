package fr.group5.magellangpt.data.repositories

import fr.group5.magellangpt.data.local.dao.MessageDao
import fr.group5.magellangpt.data.local.entities.MessageEntity
import fr.group5.magellangpt.data.remote.ApiService
import fr.group5.magellangpt.domain.models.Message
import fr.group5.magellangpt.domain.models.MessageSender
import fr.group5.magellangpt.domain.repositories.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.java.KoinJavaComponent.get
import java.util.Date

class MessageRepositoryImpl(
    private val messageDao: MessageDao = get(MessageDao::class.java),
    private val apiService: ApiService = get(ApiService::class.java)
) : MessageRepository {
    override fun getMessages(): Flow<List<Message>> {
        return messageDao.getMessages().map {
            it.map { messageEntity ->
                Message(
                    id = messageEntity.id,
                    content = messageEntity.content,
                    sender = messageEntity.sender,
                    date = messageEntity.date
                )
            }
        }
    }

    override suspend fun sendMessage(content: String) {
        val message = MessageEntity(
            content = content,
            sender = MessageSender.USER,
            date = Date()
        )
        messageDao.insertMessage(message)

        val responseMessage = apiService.sendMessage(content)

        val responseMessageEntity = MessageEntity(
            content = responseMessage,
            sender = MessageSender.AI,
            date = Date()
        )

        messageDao.insertMessage(responseMessageEntity)
    }
}