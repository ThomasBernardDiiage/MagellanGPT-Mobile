package fr.group5.magellangpt.data.remote.dto.up

import com.google.gson.annotations.SerializedName

data class MessageDtoUp(
    @SerializedName("Message")
    val message : String,

    @SerializedName("SemanticModelKey")
    val model : String
)