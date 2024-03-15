package fr.group5.magellangpt.domain.models

import com.google.gson.annotations.SerializedName

enum class MessageSender {
    @SerializedName("User")
    USER,

    @SerializedName("System")
    AI
}