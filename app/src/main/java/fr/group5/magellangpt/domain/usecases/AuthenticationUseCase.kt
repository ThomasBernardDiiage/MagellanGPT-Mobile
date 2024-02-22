package fr.group5.magellangpt.domain.usecases

import android.content.Context
import android.util.Log
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.SignInParameters
import com.microsoft.identity.client.exception.MsalException
import fr.group5.magellangpt.R
import fr.group5.magellangpt.common.helpers.ResourcesHelper
import fr.group5.magellangpt.domain.models.Resource
import fr.group5.magellangpt.presentation.MainActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get


class AuthenticationUseCase(
    private val ioDispatcher: CoroutineDispatcher = get(CoroutineDispatcher::class.java),
    private val resourcesHelper: ResourcesHelper = get(ResourcesHelper::class.java)
) {
    private var client : ISingleAccountPublicClientApplication? = null

    suspend fun login(activity: MainActivity) : Resource<Unit> = withContext(ioDispatcher) {
        try {
            client = PublicClientApplication.createSingleAccountPublicClientApplication(
                activity,
                R.raw.auth_config)

            val parameters = SignInParameters
                .builder()
                .withActivity(activity)
                .withScope("User.Read")
                .withCallback(object : AuthenticationCallback {
                    override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                        Log.d("AuthenticationUseCase", "Successfully signed in")
                    }

                    override fun onError(exception: MsalException?) {
                        Log.e("AuthenticationUseCase", exception?.message ?: "An error occurred")
                    }

                    override fun onCancel() {
                        Log.d("AuthenticationUseCase", "User cancelled signing in")
                    }
                })

            client?.signIn(parameters.build())



            delay(2000)

            Resource.Success(Unit)
        }
        catch (e : Exception){
            Resource.Error(resourcesHelper.getString(R.string.error_occured))
        }
    }

    suspend fun logout() : Resource<Unit> = withContext(ioDispatcher) {
        try {
            client?.signOut()
            Resource.Success(Unit)
        }
        catch (e : Exception){
            Resource.Error(resourcesHelper.getString(R.string.error_occured))
        }
    }
}