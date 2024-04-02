package fr.group5.magellangpt.domain.usecases

import fr.group5.magellangpt.domain.repositories.QuotaRepository
import org.koin.java.KoinJavaComponent.get

class GetCurrentQuotaUseCase(
    private val quotaRepository: QuotaRepository = get(QuotaRepository::class.java)
) {
    suspend operator fun invoke() =
        quotaRepository.getCurrentQuota()
}