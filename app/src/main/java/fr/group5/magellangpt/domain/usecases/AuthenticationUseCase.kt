package fr.group5.magellangpt.domain.usecases

import android.util.Log
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.domain.repositories.AuthenticationRepository
import fr.group5.magellangpt.presentation.MainActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get

class AuthenticationUseCase(
    private val ioDispatcher: CoroutineDispatcher = get(CoroutineDispatcher::class.java),
    private val resourcesHelper: ResourcesHelper = get(ResourcesHelper::class.java),
    private val authenticationRepository: AuthenticationRepository = get(AuthenticationRepository::class.java)
) {

    suspend fun login(
        activity: MainActivity,
        onSuccess : () -> Unit,
        onError : (String) -> Unit,
        onCancel : () -> Unit
    ) = withContext(ioDispatcher) {
        try {
            authenticationRepository.login(
                activity= activity,
                onSuccess = onSuccess,
                onError = { onError(resourcesHelper.getString(R.string.error_occured)) },
                onCancel = onCancel)
        }
        catch (e : Exception){
            onError(resourcesHelper.getString(R.string.error_occured))
            Log.e("AuthenticationUseCase", e.message ?: "An error occurred")
        }
    }

    suspend fun logout() = withContext(ioDispatcher){
        try {
            authenticationRepository.logout()
        }
        catch (e : Exception){
            Log.e("AuthenticationUseCase", e.message ?: "An error occurred")
        }
    }

    suspend fun userConnected(onResult : (Boolean) -> Unit) = withContext(ioDispatcher){
        authenticationRepository.userConnected {
            onResult(it)
        }
    }
}