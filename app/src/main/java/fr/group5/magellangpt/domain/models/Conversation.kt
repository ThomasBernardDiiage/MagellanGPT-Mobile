package fr.group5.magellangpt.domain.models

import java.util.Date
import java.util.UUID

data class Conversation(
    val id : UUID,
    val title : String,
    val lastModificationDate : Date,
)