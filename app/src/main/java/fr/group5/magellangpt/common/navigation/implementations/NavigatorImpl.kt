package fr.group5.magellangpt.common.navigation.implementations

import fr.group5.magellangpt.common.navigation.Navigator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigatorImpl : Navigator {
    private val _sharedFlow = MutableSharedFlow<Navigator.NavigationEvent>(extraBufferCapacity = 1)
    override val sharedFlow = _sharedFlow.asSharedFlow()

    override fun navigateTo(route: Navigator.Destination, popupTo : String?, inclusive : Boolean) {
        _sharedFlow.tryEmit(Navigator.NavigationEvent.NavigateTo(route, popupTo, inclusive))
    }

    override fun goBack() {
        _sharedFlow.tryEmit(Navigator.NavigationEvent.GoBack)
    }
}