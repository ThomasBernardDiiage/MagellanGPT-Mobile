package fr.group5.magellangpt.domain.usecases

import fr.group5.magellangpt.domain.models.Message
import fr.group5.magellangpt.domain.repositories.ConversationRepository
import kotlinx.coroutines.flow.Flow
import org.koin.java.KoinJavaComponent.get

class GetMessagesUseCase(
    private val conversationRepository: ConversationRepository = get(ConversationRepository::class.java)
) {
    operator fun invoke() : Flow<List<Message>> {
        return conversationRepository.getMessages()
    }
}