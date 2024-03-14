package fr.group5.magellangpt.data.repositories

import android.util.Log
import com.google.gson.JsonSyntaxException
import fr.group5.magellangpt.data.local.dao.MessageDao
import fr.group5.magellangpt.data.local.entities.MessageEntity
import fr.group5.magellangpt.data.remote.ApiService
import fr.group5.magellangpt.domain.models.Message
import fr.group5.magellangpt.domain.models.MessageSender
import fr.group5.magellangpt.domain.repositories.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.koin.java.KoinJavaComponent.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.Date


class MessageRepositoryImpl(
    private val messageDao: MessageDao = get(MessageDao::class.java),
    private val apiService: ApiService = get(ApiService::class.java),
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

        val responseMessageEntity = MessageEntity(
            content = "",
            sender = MessageSender.AI,
            date = Date()
        )

        val id = messageDao.insertMessage(responseMessageEntity)

        val response = apiService.sendMessage(content)

        messageDao.updateMessageContent(id, response.filterNotNull().joinToString(""))
    }

//    // https://github.com/lambiengcode/compose-chatgpt-kotlin-android-chatbot/blob/main/app/src/main/java/com/chatgptlite/wanted/data/remote/OpenAIRepositoryImpl.kt
//    private suspend fun getResponse(content : String) : Flow<String> {
//        return callbackFlow {
//            withContext(Dispatchers.IO){
//                val response = apiService.sendMessage(content).execute()
//                if (response.isSuccessful){
//                    val input = response.body()?.byteStream()?.bufferedReader() ?: throw Exception()
//                    try {
//                        while (true) {
//                            val line = withContext(Dispatchers.IO) {
//                                input.readLine()
//                            } ?: continue
//                            try {
//                                Log.i("ChatGPT Lite", line)
//                                trySend(line)
//                            }
//                            catch (e: Exception) {
//                                e.printStackTrace()
//                                Log.e("ChatGPT Lite BUG", e.toString())
//                            }
//                        }
//                    } catch (e: IOException) {
//                        Log.e("ChatGPT Lite BUG", e.toString())
//                        throw Exception(e)
//                    } finally {
//                        withContext(Dispatchers.IO) {
//                            input.close()
//                        }
//
//                        close()
//                    }
//                }
//                else {
//                    Log.e("MessageRepositoryImpl", "Error: ${response.code()}")
//                }
//            }
//        }
//    }
}