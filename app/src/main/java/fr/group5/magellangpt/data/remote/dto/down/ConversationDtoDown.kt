package fr.group5.magellangpt.data.remote.dto.down

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ConversationDtoDown(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("lastMessageDate")
    val lastMessageDate: Date,
)