package fr.group5.magellangpt.domain.usecases

import fr.group5.magellangpt.domain.repositories.MessageRepository
import org.koin.java.KoinJavaComponent.get

class SendMessageUseCase(
    private val messageRepository: MessageRepository = get(MessageRepository::class.java)
) {
    suspend operator fun invoke(content: String) {
        messageRepository.sendMessage(content)
    }
}