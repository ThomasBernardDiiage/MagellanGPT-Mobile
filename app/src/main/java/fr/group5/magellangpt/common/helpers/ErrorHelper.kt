package fr.group5.magellangpt.common.helpers

import kotlinx.coroutines.flow.SharedFlow

interface ErrorHelper {
    val sharedFlow : SharedFlow<Error>

    suspend fun onError(error: Error)

    data class Error(val message: String)
}