package fr.group5.magellangpt.data.remote.dto.down

import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class ConversationDtoDown(
    @SerializedName("id")
    val id: UUID,

    @SerializedName("title")
    val title: String,

    @SerializedName("lastModificationDate")
    val lastModificationDate : Date,

    @SerializedName("messages")
    val messages: List<ConversationMessageDtoDown>
)