package fr.group5.magellangpt.data.repositories

import fr.group5.magellangpt.data.remote.ApiService
import fr.group5.magellangpt.domain.models.Model
import fr.group5.magellangpt.domain.repositories.ModelRepository
import org.koin.java.KoinJavaComponent.get

class ModelRepositoryImpl(
    private val apiService: ApiService = get(ApiService::class.java)
) : ModelRepository {
    override suspend fun getAvailableModels(): List<Model> {
        val modelsDtoDown = apiService.getModels()

        return modelsDtoDown.map {
            Model(
                id = it.id,
                name = it.name,
                index = it.index
            )
        }
    }
}