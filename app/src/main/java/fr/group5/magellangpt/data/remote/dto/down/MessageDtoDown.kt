package fr.group5.magellangpt.data.remote.dto.down

import com.google.gson.annotations.SerializedName
import fr.group5.magellangpt.domain.models.MessageSender
import java.util.Date

data class MessageDtoDown(
    @SerializedName("id")
    val id : String,

    @SerializedName("conversationId")
    val conversationId : Int,

    @SerializedName("message")
    val message : String,

    @SerializedName("chatRole")
    val chatRole : MessageSender,

    @SerializedName("dateTimeMessage")
    val dateTimeMessage : Date
)

//{
//    "id": "df1e933a-9ff6-46c9-a393-66a1d32dbb78",
//    "userId": "1",
//    "conversationId": "10",
//    "message": "Bonjour",
//    "chatRole": "User",
//    "dateTimeMessage": "2024-03-14T14:06:24Z"
//}