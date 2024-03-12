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
import com.microsoft.identity.client.SilentAuthenticationCallback
import com.microsoft.identity.client.exception.MsalException
import fr.group5.magellangpt.R
import fr.group5.magellangpt.domain.models.User
import fr.group5.magellangpt.domain.repositories.AuthenticationRepository
import org.koin.java.KoinJavaComponent.get

class AuthenticationRepositoryImpl(
    private val context : Context = get(Context::class.java)
) : AuthenticationRepository {

    private var account: IAccount? = null
    private val authorityUrl : String = "https://login.microsoftonline.com/14bc5219-40ca-4d62-a8e4-7c97c1236349"


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
                        Log.e("LoginUseCase", exception?.message ?: "An error occurred")
                        onError()
                    }

                    override fun onCancel() {
                        Log.d("LoginUseCase", "User cancelled signing in")
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
                    Log.d("LoginUseCase", "Account changed")
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
                    Log.d("LoginUseCase", "Account changed")
                }

                override fun onError(exception: MsalException) {
                    Log.i("Logout Error", exception.toString())
                }
            }
        )
    }

    override suspend fun getCurrentUser(onResult : (user : User) -> Unit) {
        client.getCurrentAccountAsync(
            object : ISingleAccountPublicClientApplication.CurrentAccountCallback {
                override fun onAccountLoaded(activeAccount: IAccount?) {
                    account = activeAccount
                    val displayName = activeAccount?.claims?.get("name") as String?
                    val email = activeAccount?.claims?.get("preferred_username") as String?
                    val user = User(
                        firstname = displayName?.split(" ")?.get(0) ?: "",
                        lastname = displayName?.split(" ")?.get(1) ?: "",
                        email = email ?: ""
                    )

                    getToken()
                    onResult(user)
                }

                override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {
                    Log.d("LoginUseCase", "Account changed")
                }

                override fun onError(exception: MsalException) {
                    Log.i("Logout Error", exception.toString())
                }
            }
        )
    }

    private fun getToken(){
        val scopes = arrayOf("User.Read") // Définissez les scopes nécessaires pour votre token

        client.acquireTokenSilentAsync(scopes, authorityUrl, object : SilentAuthenticationCallback {
            override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                val accessToken = authenticationResult?.accessToken
                Log.i("Token Request Success", accessToken ?: "")
            }

            override fun onError(exception: MsalException?) {
                Log.e("Token Request Error", exception?.message?.toString() ?: "")
            }
        })
    }
}