package fr.group5.magellangpt.data.remote.dto.down

import com.google.gson.annotations.SerializedName

data class TcuQuestionDtoDown(
    @SerializedName("index")
    val index : Int,

    @SerializedName("text")
    val text : String
)