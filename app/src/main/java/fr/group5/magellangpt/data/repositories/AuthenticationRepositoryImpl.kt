package fr.group5.magellangpt.data.repositories

import android.app.Activity
import android.content.Context
import android.util.Log
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.SignInParameters
import com.microsoft.identity.client.exception.MsalException
import fr.group5.magellangpt.R
import fr.group5.magellangpt.domain.repositories.AuthenticationRepository
import org.koin.java.KoinJavaComponent.get

class AuthenticationRepositoryImpl(
    private val context : Context = get(Context::class.java)
) : AuthenticationRepository {

    private var client : ISingleAccountPublicClientApplication? = null


    override fun login(
        activity : Activity,
        onSuccess : () -> Unit,
        onCancel : () -> Unit,
        onError : () -> Unit
    ){
        client = PublicClientApplication.createSingleAccountPublicClientApplication(
            context,
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

        client?.acquireToken(activity, arrayOf("User.Read"), object : AuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                Log.d("AuthenticationUseCase", "Successfully signed in, ${authenticationResult?.accessToken}")
                onSuccess()
            }

            override fun onError(exception: MsalException?) {
                Log.e("AuthenticationUseCase", exception?.message ?: "An error occurred")
                onError()
            }

            override fun onCancel() {
                Log.d("AuthenticationUseCase", "User cancelled signing in")
                onCancel()
            }
        })
    }

    override fun logout() {
        client?.signOut()
    }
}