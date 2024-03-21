package fr.group5.magellangpt.data.remote.dto.down

import com.google.gson.annotations.SerializedName

data class ModelDtoDown(
    @SerializedName("id")
    val id : String,

    @SerializedName("name")
    val name : String,

    @SerializedName("index")
    val index : Int
)