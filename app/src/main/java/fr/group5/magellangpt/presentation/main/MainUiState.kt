package fr.group5.magellangpt.presentation.main

import fr.group5.magellangpt.domain.models.Message

data class MainUiState(
    val message : String = "",

    val conversationQuery : String = "",

    val messages : List<Message> = emptyList(),

    val firstname : String = "-",
    val lastname : String = "-",
    val email : String = "-",
)