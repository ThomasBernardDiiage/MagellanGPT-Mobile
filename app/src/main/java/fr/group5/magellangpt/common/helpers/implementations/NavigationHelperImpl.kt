package fr.group5.magellangpt.common.helpers.implementations

import fr.group5.magellangpt.common.helpers.NavigationHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationHelperImpl : NavigationHelper {
    private val _sharedFlow = MutableSharedFlow<NavigationHelper.NavigationEvent>(extraBufferCapacity = 1)
    override val sharedFlow = _sharedFlow.asSharedFlow()

    override fun navigateTo(route: NavigationHelper.Destination, popupTo : String?, inclusive : Boolean) {
        _sharedFlow.tryEmit(NavigationHelper.NavigationEvent.NavigateTo(route, popupTo, inclusive))
    }

    override fun goBack() {
        _sharedFlow.tryEmit(NavigationHelper.NavigationEvent.GoBack)
    }
}