package fr.group5.magellangpt.data.repositories

import fr.group5.magellangpt.data.remote.ApiService
import fr.group5.magellangpt.domain.models.Model
import fr.group5.magellangpt.domain.repositories.ModelRepository
import org.koin.java.KoinJavaComponent.get

class ModelRepositoryImpl(
    private val apiService: ApiService = get(ApiService::class.java)
) : ModelRepository {
    override suspend fun getAvailableModels(): List<Model> {

        return listOf(
            Model(
                id = "gpt-3",
                name = "GPT-3",
                index = 0
            ),
            Model(
                id = "gpt-4",
                name = "GPT-4",
                index = 1
            )
        )

        val models = apiService.getModels()

        return models.map {
            Model(
                id = it.id,
                name = it.name,
                index = it.index
            )
        }
    }
}