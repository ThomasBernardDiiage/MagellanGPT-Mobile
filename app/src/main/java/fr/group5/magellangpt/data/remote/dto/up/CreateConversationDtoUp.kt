package fr.group5.magellangpt.data.remote.dto.up

import com.google.gson.annotations.SerializedName

data class CreateConversationDtoUp(
    @SerializedName("title")
    val title : String,

    @SerializedName("prompt")
    val prompt : String
)