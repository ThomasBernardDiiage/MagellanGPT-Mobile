package fr.group5.magellangpt.data.remote.dto.down

import com.google.gson.annotations.SerializedName
import fr.group5.magellangpt.domain.models.MessageSender
import java.util.Date

data class MessageDtoDown(
    @SerializedName("sender")
    val sender : MessageSender,

    @SerializedName("text")
    val text : String,

    @SerializedName("model")
    val model : String,

    @SerializedName("filesNames")
    val filesNames : List<String>,

    @SerializedName("date")
    val date : Date
)

//{
//    "id": "4501c401-79aa-4670-9b6e-24f91218f391",
//    "sender": 1,
//    "text": "La femme de Nicolas Sarkozy est Carla Bruni.",
//    "model": "gpt-3",
//    "date": "2024-03-20T13:33:20.356004+01:00"
//}