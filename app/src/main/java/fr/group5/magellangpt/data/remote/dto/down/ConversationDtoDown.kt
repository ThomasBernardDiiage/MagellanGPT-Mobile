package fr.group5.magellangpt.data.remote.dto.down

import java.util.UUID

data class ConversationDtoDown(
    val id: UUID,
    val title: String,
    val messages: List<ConversationMessageDtoDown>
)