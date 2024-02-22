package fr.group5.magellangpt.presentation.main

import fr.group5.magellangpt.domain.models.Message

data class MainUiState(
    val query : String = "",

    val messages : List<Message> = emptyList(),
)