package fr.group5.magellangpt.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ModelEntity(
    @PrimaryKey
    val id : String,
    val name : String,
    val index : Int
)