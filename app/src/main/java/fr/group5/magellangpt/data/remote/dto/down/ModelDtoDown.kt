package fr.group5.magellangpt.data.remote.dto.down

import com.google.gson.annotations.SerializedName

data class ModelDtoDown(
    @SerializedName("modelId")
    val id : String,

    @SerializedName("modelName")
    val name : String,

    @SerializedName("modelIndex")
    val index : Int
)