package fr.group5.magellangpt.common.navigation

import kotlinx.coroutines.flow.SharedFlow

interface Navigator {
    val sharedFlow : SharedFlow<NavigationEvent>
    fun navigateTo(route: Destination, popupTo : String? = null, inclusive : Boolean = false)
    fun goBack()

    sealed class NavigationEvent {
        data class NavigateTo(val destination: Destination, val popupTo : String? = null, val inclusive: Boolean = false) : NavigationEvent()
        data object GoBack : NavigationEvent()
    }

    sealed class Destination(val route: String) {
        data object Login : Destination("login")
        data object Main : Destination("main")
    }
}
