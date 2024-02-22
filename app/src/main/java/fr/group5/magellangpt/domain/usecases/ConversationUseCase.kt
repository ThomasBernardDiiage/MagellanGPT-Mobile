package fr.group5.magellangpt.domain.usecases

import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.domain.models.Message
import fr.group5.magellangpt.domain.models.MessageSender
import fr.group5.magellangpt.domain.models.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get

class ConversationUseCase(
    private val ioDispatcher: CoroutineDispatcher = get(CoroutineDispatcher::class.java),
    private val resourcesHelper: ResourcesHelper = get(ResourcesHelper::class.java)
) {

    suspend fun getConversationMessages() : Resource<List<Message>> = withContext(ioDispatcher){
        try {
            val messages = listOf(
                Message(
                    content = "Hello Magellan GPT",
                    sender = MessageSender.USER),
                Message(
                    content = "I am Magellan, your personal guide",
                    sender = MessageSender.AI)
            )

            Resource.Success(messages)
        }
        catch (e : Exception){
            Resource.Error(resourcesHelper.getString(R.string.error_occured))
        }

    }
}