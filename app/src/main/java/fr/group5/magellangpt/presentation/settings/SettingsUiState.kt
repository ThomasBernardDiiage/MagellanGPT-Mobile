package fr.group5.magellangpt.presentation.settings

data class SettingsUiState(
    val firstname : String = "-",
    val lastname : String = "-",
    val email : String = "-",


    val currentQuota : Int = 0,
    val maxQuota : Int = 1000
)