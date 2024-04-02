package fr.group5.magellangpt.data.remote.dto.down

import com.google.gson.annotations.SerializedName

data class TcuAnalysisDtoDown(
    @SerializedName("index")
    val index : Int,

    @SerializedName("question")
    val question : String,

    @SerializedName("answer")
    val answer : String
)