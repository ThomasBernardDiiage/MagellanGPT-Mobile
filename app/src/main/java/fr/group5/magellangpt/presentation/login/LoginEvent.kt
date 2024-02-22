package fr.group5.magellangpt.presentation.login

sealed class LoginEvent {
    data object OnLogin : LoginEvent()
}