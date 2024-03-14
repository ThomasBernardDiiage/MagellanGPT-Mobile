package fr.group5.magellangpt.data.remote.dto.down

import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class ConversationDtoDown(
    val id: UUID,
    val title: String,
    val lastMessageDate: Date,
)