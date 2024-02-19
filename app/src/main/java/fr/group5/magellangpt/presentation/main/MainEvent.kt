package fr.group5.magellangpt.presentation.main

sealed class MainEvent {
    data class OnQueryChanged(val query: String) : MainEvent()
}