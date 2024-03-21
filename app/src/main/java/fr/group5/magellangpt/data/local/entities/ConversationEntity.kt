package fr.group5.magellangpt.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class ConversationEntity(
    @PrimaryKey
    val id: UUID,
    val title: String,
    val lastModificationDate: Date,
)