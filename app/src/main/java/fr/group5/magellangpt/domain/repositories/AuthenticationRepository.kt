package fr.group5.magellangpt.domain.repositories

import android.app.Activity

interface AuthenticationRepository {

    fun login(activity : Activity, onSuccess : () -> Unit, onCancel : () -> Unit, onError : () -> Unit)

    suspend fun logout()
}