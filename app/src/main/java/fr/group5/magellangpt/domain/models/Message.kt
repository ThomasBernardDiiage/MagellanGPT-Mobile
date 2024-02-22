package fr.group5.magellangpt.domain.models

data class Message(
    val content : String,
    val sender : MessageSender
)