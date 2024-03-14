package fr.group5.magellangpt.presentation.main

import fr.group5.magellangpt.domain.models.Model

sealed class MainEvent {
    data object OnAppearing : MainEvent()
    data class OnQueryChanged(val query: String) : MainEvent()


    data class OnSendMessage(val message: String) : MainEvent()

    data object OnLogout : MainEvent()

    data class OnModelSelected(val model: Model) : MainEvent()
}