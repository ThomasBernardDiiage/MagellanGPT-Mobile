package fr.group5.magellangpt.data.remote.dto.down

import com.google.gson.annotations.SerializedName

data class QuotaDtoDown(
    val currentQuota : Double,
    val maxQuota : Double
)