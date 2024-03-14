package fr.group5.magellangpt.domain.usecases

import fr.group5.magellangpt.domain.models.Model
import fr.group5.magellangpt.domain.repositories.ModelRepository
import org.koin.java.KoinJavaComponent.get

class GetAvailableModelsUseCase(
    private val modelRepository : ModelRepository = get(ModelRepository::class.java)
) {
    suspend operator fun invoke() : List<Model> {
        return modelRepository.getAvailableModels().sortedBy { it.index }
    }
}