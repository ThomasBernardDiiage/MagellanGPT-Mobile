package fr.group5.magellangpt.presentation.login

import fr.group5.magellangpt.presentation.MainActivity

sealed class LoginEvent {
    data object OnAppearing : LoginEvent()
    data class OnLogin(val activity : MainActivity) : LoginEvent()
}