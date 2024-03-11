package fr.group5.magellangpt.domain.usecases

import fr.group5.magellangpt.domain.models.User
import fr.group5.magellangpt.domain.repositories.AuthenticationRepository
import org.koin.java.KoinJavaComponent.get

class GetCurrentUserUseCase(
    private val authenticationRepository : AuthenticationRepository = get(AuthenticationRepository::class.java)
) {
    suspend operator fun invoke(onResult : (user : User) -> Unit){
        authenticationRepository.getCurrentUser(onResult)
    }
}