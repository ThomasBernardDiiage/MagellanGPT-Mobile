package fr.group5.magellangpt.domain.repositories

import fr.group5.magellangpt.domain.models.Quota
import kotlinx.coroutines.flow.Flow

interface QuotaRepository {
    suspend fun getCurrentQuota(): Flow<Quota>
}