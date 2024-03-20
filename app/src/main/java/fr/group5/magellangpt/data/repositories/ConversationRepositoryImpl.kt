package fr.group5.magellangpt.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.net.http.HttpException
import android.provider.OpenableColumns
import fr.group5.magellangpt.common.helpers.PreferencesHelper
import fr.group5.magellangpt.data.local.dao.ConversationDao
import fr.group5.magellangpt.data.local.dao.MessageDao
import fr.group5.magellangpt.data.local.dao.ModelDao
import fr.group5.magellangpt.data.local.entities.ConversationEntity
import fr.group5.magellangpt.data.local.entities.MessageEntity
import fr.group5.magellangpt.data.remote.ApiService
import fr.group5.magellangpt.data.remote.dto.up.CreateConversationDtoUp
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
                    filesNames = messageEntity.filesNames,
                    date = messageEntity.date)
            }
        }


    override suspend fun getConversations(): List<Conversation> {
        val conversationsDtoDown = apiService.getConversations()
        val conversationsEntity = conversationsDtoDown.map {
            ConversationEntity(id = it.id, title = it.title, lastModificationDate = it.lastModificationDate)
        }

        conversationDao.insertConversations(conversationsEntity)

        return conversationsDtoDown.map {
            Conversation(id = it.id, title = it.title, it.lastModificationDate)
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
                filesNames = it.filesNames,
                date = it.date)
        }

        messageDao.insertMessages(messagesEntity)
    }

    override suspend fun sendMessage(conversationId : UUID, content: String, uris : List<Uri>) {
        val message = MessageEntity(content = content, sender = MessageSender.USER, model = null, date = Date(), filesNames = uris.map { getFileName(it) } as ArrayList<String>)

        val userMessageId = messageDao.insertMessage(message)

        val filesParts = uris.mapIndexed { index, uri ->
            prepareFilePart("files", uri)
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
            filesNames = response.body()!!.filesNames as ArrayList<String>,
            date = response.body()!!.date)

        messageDao.insertMessage(responseMessageEntity)
    }

    override suspend fun createConversation(name: String, prePrompt: String): Conversation {
        val dtoUp = CreateConversationDtoUp(title = name, prePrompt = prePrompt)

        val result = apiService.createConversation(dtoUp)

        return Conversation(id = result.id, title = result.title, lastModificationDate = result.lastModificationDate)
    }

    private fun prepareFilePart(partName: String, uri: Uri): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "tempFile.pdf")
        FileOutputStream(file).use { outputStream ->
            inputStream?.copyTo(outputStream)
        }

        val requestFile = RequestBody.create("application/pdf".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }
}