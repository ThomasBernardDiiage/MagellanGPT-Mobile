package fr.group5.magellangpt.domain.usecases

import fr.group5.magellangpt.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get

class UserConnectedUseCase(
    private val authenticationRepository: AuthenticationRepository = get(AuthenticationRepository::class.java),
    private val ioDispatcher: CoroutineDispatcher = get(CoroutineDispatcher::class.java)
) {
    suspend operator fun invoke(onResult : (Boolean) -> Unit) = withContext(ioDispatcher){
        authenticationRepository.userConnected {
            onResult(it)
        }
    }
}