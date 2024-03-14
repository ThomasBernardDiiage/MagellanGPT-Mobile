package fr.group5.magellangpt.domain.models

import java.util.Date

data class Conversation(
    val id : Int,
    val title : String,
    val lastMessageDate : Date,
)