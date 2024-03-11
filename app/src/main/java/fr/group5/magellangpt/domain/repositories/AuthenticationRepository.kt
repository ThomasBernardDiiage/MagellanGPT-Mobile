package fr.group5.magellangpt.domain.repositories

import android.app.Activity
import fr.group5.magellangpt.domain.models.User

interface AuthenticationRepository {

    /**
     * Login the user
     * @param activity the activity (needed for the MSAL library)
     * @param onSuccess the success callback (the user is logged in)
     * @param onCancel the cancel callback (the user cancelled the login)
     * @param onError the error callback (an error occurred)
     */
    fun login(activity : Activity, onSuccess : () -> Unit, onCancel : () -> Unit, onError : () -> Unit)

    /**
     * Logout the user, clear the credentials
     */
    suspend fun logout()

    /**
     * Check if the user is connected
     * @param onResult the result callback
     */
    suspend fun userConnected(onResult : (Boolean) -> Unit)

    suspend fun getCurrentUser(onResult :(user : User) -> Unit)
}