package fr.group5.magellangpt.presentation.main

import fr.group5.magellangpt.domain.models.Message
import java.util.Date

data class MainUiState(
    val message : String = "",
    val typing : Boolean = false,

    val conversationQuery : String = "",

    val messages : Map<Date, List<Message>> = emptyMap(),

    val firstname : String = "-",
    val lastname : String = "-",
    val email : String = "-",
)