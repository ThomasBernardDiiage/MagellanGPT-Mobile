package fr.group5.magellangpt.common.helpers

import kotlinx.coroutines.flow.SharedFlow

interface NavigationHelper {
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
        data object Settings : Destination("settings")
        data object KnowledgeBase : Destination("knowledgebase")
        data object Tcu : Destination("tcu")
    }
}
