package fr.group5.magellangpt.data.repositories

import fr.group5.magellangpt.common.helpers.PreferencesHelper
import fr.group5.magellangpt.data.local.dao.ConversationDao
import fr.group5.magellangpt.data.local.dao.MessageDao
import fr.group5.magellangpt.data.local.dao.ModelDao
import fr.group5.magellangpt.data.local.entities.ConversationEntity
import fr.group5.magellangpt.data.local.entities.MessageEntity
import fr.group5.magellangpt.data.remote.ApiService
import fr.group5.magellangpt.data.remote.dto.up.MessageDtoUp
import fr.group5.magellangpt.domain.models.Conversation
import fr.group5.magellangpt.domain.models.Message
import fr.group5.magellangpt.domain.models.MessageSender
import fr.group5.magellangpt.domain.repositories.ConversationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get
import java.util.Date
import java.util.UUID


class ConversationRepositoryImpl(
    private val messageDao: MessageDao = get(MessageDao::class.java),
    private val modelDao: ModelDao = get(ModelDao::class.java),
    private val conversationDao: ConversationDao = get(ConversationDao::class.java),
    private val apiService: ApiService = get(ApiService::class.java),
    private val preferencesHelper: PreferencesHelper = get(PreferencesHelper::class.java)
) : ConversationRepository {

    override fun getMessages(): Flow<List<Message>> =
        messageDao.getMessages().map {
            it.map { messageEntity ->
                Message(
                    id = messageEntity.id,
                    content = messageEntity.content,
                    sender = messageEntity.sender,
                    model = messageEntity.model,
                    date = messageEntity.date)
            }
        }


    override suspend fun getConversations(): List<Conversation> {
        val conversationsDtoDown = apiService.getConversations()
        val conversationsEntity = conversationsDtoDown.map {
            ConversationEntity(id = it.id, title = it.title, lastMessageDate = it.lastMessageDate)
        }

        conversationDao.insertConversations(conversationsEntity)

        return conversationsDtoDown.map {
            Conversation(id = it.id, title = it.title, lastMessageDate = it.lastMessageDate)
        }
    }


    override suspend fun getMessages(conversationId : UUID) {
        messageDao.nuke()

        val messagesDtoDown = apiService.getConversationMessages(conversationId)

        val messagesEntity = messagesDtoDown.map {

            val selectedModel = it.model?.let { modelDao.getModel(it) }

            MessageEntity(
                content = it.message,
                sender = it.chatRole,
                model = selectedModel?.name,
                date = it.dateTimeMessage)
        }

        messageDao.insertMessages(messagesEntity)
    }



    override suspend fun sendMessage(conversationId : UUID, content: String) {
        val message = MessageEntity(
            content = content,
            sender = MessageSender.USER,
            model = null,
            date = Date())

        messageDao.insertMessage(message)

        val dtoUp = MessageDtoUp(message = content, model = preferencesHelper.selectedModelId)

        val response = apiService.postMessage(conversationId, dtoUp)
        val selectedModel = modelDao.getModel(preferencesHelper.selectedModelId)

        val responseMessageEntity = MessageEntity(
            content = response,
            sender = MessageSender.AI,
            model = selectedModel.name,
            date = Date())

        messageDao.insertMessage(responseMessageEntity)
    }

    override suspend fun sendMessage(content: String) {
        val message = MessageEntity(
            content = content,
            sender = MessageSender.USER,
            model = null,
            date = Date()
        )
        messageDao.insertMessage(message)

        val dtoUp = MessageDtoUp(message = content, model = preferencesHelper.selectedModelId)

        val response = apiService.postMessage(UUID.randomUUID(), dtoUp)
        val selectedModel = modelDao.getModel(preferencesHelper.selectedModelId)


        val responseMessageEntity = MessageEntity(
            content = response,
            sender = MessageSender.AI,
            model = selectedModel.name,
            date = Date())

        messageDao.insertMessage(responseMessageEntity)

    }
}