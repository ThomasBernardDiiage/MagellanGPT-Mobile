package fr.group5.magellangpt.data.repositories

import android.app.Activity
import android.content.Context
import android.util.Log
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
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

    private var account: IAccount? = null
    private val client : ISingleAccountPublicClientApplication by lazy {
        PublicClientApplication.createSingleAccountPublicClientApplication(context, R.raw.auth_config)
    }

    override fun login(
        activity : Activity,
        onSuccess : () -> Unit,
        onCancel : () -> Unit,
        onError : () -> Unit
    ){
        fun loginMsal(){
            val parameters = SignInParameters
                .builder()
                .withActivity(activity)
                .withScope("User.Read")
                .withCallback(object : AuthenticationCallback {
                    override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                        account = authenticationResult?.account
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

            client.signIn(parameters.build())
        }

        client.getCurrentAccountAsync(
            object : ISingleAccountPublicClientApplication.CurrentAccountCallback {
                override fun onAccountLoaded(activeAccount: IAccount?) {
                    account = activeAccount
                    if (account == null)
                        loginMsal()
                    else
                        onSuccess()
                }

                override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {
                    Log.d("AuthenticationUseCase", "Account changed")
                }

                override fun onError(exception: MsalException) {
                    Log.i("Logout Error", exception.toString())
                    onError()
                }
            }
        )
    }

    override suspend fun logout() {
        client.signOut()
        account = null
    }

    override suspend fun userConnected(onResult : (Boolean) -> Unit) {
        client.getCurrentAccountAsync(
            object : ISingleAccountPublicClientApplication.CurrentAccountCallback {
                override fun onAccountLoaded(activeAccount: IAccount?) {
                    onResult(activeAccount != null)
                }

                override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {
                    Log.d("AuthenticationUseCase", "Account changed")
                }

                override fun onError(exception: MsalException) {
                    Log.i("Logout Error", exception.toString())
                }
            }
        )
    }
}