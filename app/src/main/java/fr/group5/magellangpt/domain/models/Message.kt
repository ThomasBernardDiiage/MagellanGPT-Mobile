package fr.group5.magellangpt.domain.models

import java.util.Date

data class Message(
    val id : Long,
    val content : String,
    val sender : MessageSender,
    val date : Date
)