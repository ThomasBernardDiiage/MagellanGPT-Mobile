package fr.group5.magellangpt.domain.models

import com.google.gson.annotations.SerializedName

enum class MessageSender {
    @SerializedName("0")
    USER,

    @SerializedName("1")
    AI
}