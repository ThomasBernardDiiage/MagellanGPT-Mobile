package fr.group5.magellangpt.domain.usecases

import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.extensions.to6AM
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.domain.models.Message
import fr.group5.magellangpt.domain.models.MessageSender
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.repositories.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get
import java.util.Date

class GetConversationUseCase(
    private val ioDispatcher: CoroutineDispatcher = get(CoroutineDispatcher::class.java),
    private val messageRepository: MessageRepository = get(MessageRepository::class.java),
    private val resourcesHelper: ResourcesHelper = get(ResourcesHelper::class.java)
) {
    suspend operator fun invoke() : Resource<Flow<Map<Date, List<Message>>>> = withContext(ioDispatcher){
        try {
            val messages = messageRepository.getMessages()

            Resource.Success(messages
                .map { messages ->
                    messages.filter { it.content.isNotBlank() }
                        .groupBy { message -> message.date.to6AM() }
                }
            )
        }
        catch (e : Exception){
            Resource.Error(resourcesHelper.getString(R.string.error_occured))
        }
    }
}