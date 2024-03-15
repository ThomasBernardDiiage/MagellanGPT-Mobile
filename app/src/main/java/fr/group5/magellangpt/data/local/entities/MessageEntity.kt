package fr.group5.magellangpt.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.group5.magellangpt.domain.models.MessageSender
import java.util.Date

@Entity
class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content : String,
    val sender : MessageSender,
    val model : String?,
    val date : Date
)