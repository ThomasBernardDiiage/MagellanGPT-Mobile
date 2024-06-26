package fr.group5.magellangpt.presentation.settings

sealed class SettingsEvent {

    data object OnGetUserInformation : SettingsEvent()
    data object OnLogout : SettingsEvent()

    data object OnGoToKnowledgeBase : SettingsEvent()
    data object OnGoToTCU : SettingsEvent()
}