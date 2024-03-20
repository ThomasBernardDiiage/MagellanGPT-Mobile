package fr.group5.magellangpt.data.repositories

import android.content.Context
import android.net.Uri
import android.net.http.HttpException
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.java.KoinJavaComponent.get
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import java.util.UUID


class ConversationRepositoryImpl(
    private val context: Context = get(Context::class.java),
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
            ConversationEntity(id = it.id, title = it.title)
        }

        conversationDao.insertConversations(conversationsEntity)

        return conversationsDtoDown.map {
            Conversation(id = it.id, title = it.title)
        }
    }


    override suspend fun getMessages(conversationId : UUID) {
        messageDao.nuke()

        val messagesDtoDown = apiService.getConversation(conversationId)

        val messagesEntity = messagesDtoDown.messages.map {

            val selectedModel = it.model?.let { modelDao.getModel(it) }

            MessageEntity(
                content = it.text,
                sender = it.sender,
                model = selectedModel?.name ?: "",
                date = it.date)
        }

        messageDao.insertMessages(messagesEntity)
    }



    override suspend fun sendMessage(conversationId : UUID, content: String, uris : List<Uri>) {
        val message = MessageEntity(content = content, sender = MessageSender.USER, model = null, date = Date())

        val userMessageId = messageDao.insertMessage(message)

        val dtoUp = MessageDtoUp(message = content, model = preferencesHelper.selectedModelId)

        val filesParts = uris.mapIndexed { index, uri ->
            prepareFilePart("file[$index]", uri)
        }

        val response = apiService.postMessage(
            id = conversationId,
            model = MultipartBody.Part.createFormData("Model", preferencesHelper.selectedModelId),
            message = MultipartBody.Part.createFormData("Message", content),
            saveFile = MultipartBody.Part.createFormData("SaveFile", "false"),
            files = filesParts)

        if (!response.isSuccessful){
            messageDao.deleteMessage(userMessageId)
            throw retrofit2.HttpException(response)
        }

        val selectedModel = modelDao.getModel(response.body()!!.model)

        val responseMessageEntity = MessageEntity(
            content = response.body()!!.text,
            sender = response.body()!!.sender,
            model = selectedModel?.name,
            date = response.body()!!.date)

        messageDao.insertMessage(responseMessageEntity)
    }

    override suspend fun sendMessage(content: String) {
        TODO()
//        val message = MessageEntity(
//            content = content,
//            sender = MessageSender.USER,
//            model = null,
//            date = Date()
//        )
//        messageDao.insertMessage(message)
//
//        val dtoUp = MessageDtoUp(message = content, model = preferencesHelper.selectedModelId)
//
//        val response = apiService.postMessage(UUID.randomUUID(), dtoUp)
//        val selectedModel = modelDao.getModel(preferencesHelper.selectedModelId)
//
//
//        val responseMessageEntity = MessageEntity(
//            content = response,
//            sender = MessageSender.AI,
//            model = selectedModel.name,
//            date = Date())
//
//        messageDao.insertMessage(responseMessageEntity)
    }

    private fun prepareFilePart(partName: String, uri: Uri): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "tempFile")
        FileOutputStream(file).use { outputStream ->
            inputStream?.copyTo(outputStream)
        }

        val requestFile = RequestBody.create("application/pdf".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
}