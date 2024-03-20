package fr.group5.magellangpt.data.remote.dto.down

import com.google.gson.annotations.SerializedName
import fr.group5.magellangpt.domain.models.MessageSender
import java.util.Date
import java.util.UUID

data class ConversationMessageDtoDown(
    @SerializedName("id")
    val id : UUID,

    @SerializedName("conversationId")
    val conversationId : UUID,

    @SerializedName("text")
    val text : String,

    @SerializedName("sender")
    val sender : MessageSender,

    @SerializedName("date")
    val date : Date,

    @SerializedName("modelUsedForResponse")
     val model : String?
)

//{
//    "id": "df1e933a-9ff6-46c9-a393-66a1d32dbb78",
//    "userId": "1",
//    "conversationId": "10",
//    "message": "Bonjour",
//    "chatRole": "User",
//    "dateTimeMessage": "2024-03-14T14:06:24Z"
//}