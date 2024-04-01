package fr.group5.magellangpt.domain.models

data class Quota(
    val currentQuota: Int,
    val maxQuota: Int
)