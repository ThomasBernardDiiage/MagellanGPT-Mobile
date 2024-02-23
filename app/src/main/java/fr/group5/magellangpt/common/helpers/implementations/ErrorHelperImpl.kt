package fr.group5.magellangpt.common.helpers.implementations

import fr.group5.magellangpt.common.helpers.ErrorHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ErrorHelperImpl : ErrorHelper {
    private val _sharedFlow = MutableSharedFlow<ErrorHelper.Error>(extraBufferCapacity = 1)
    override val sharedFlow = _sharedFlow.asSharedFlow()

    override suspend fun onError(error: ErrorHelper.Error) {
        _sharedFlow.emit(error)
    }
}