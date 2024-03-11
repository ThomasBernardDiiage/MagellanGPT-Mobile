package fr.group5.magellangpt.domain.usecases

import android.util.Log
import fr.group5.magellangpt.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get

class LogoutUseCase(
    private val authenticationRepository: AuthenticationRepository = get(AuthenticationRepository::class.java),
    private val ioDispatcher: CoroutineDispatcher = get(CoroutineDispatcher::class.java)
) {
    suspend operator fun invoke() = withContext(ioDispatcher){
        try {
            authenticationRepository.logout()
        }
        catch (e : Exception){
            Log.e("LoginUseCase", e.message ?: "An error occurred")
        }
    }
}