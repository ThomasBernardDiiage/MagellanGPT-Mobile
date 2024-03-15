package fr.group5.magellangpt.data.repositories

import fr.group5.magellangpt.data.local.dao.ModelDao
import fr.group5.magellangpt.data.local.entities.ModelEntity
import fr.group5.magellangpt.data.remote.ApiService
import fr.group5.magellangpt.domain.models.Model
import fr.group5.magellangpt.domain.repositories.ModelRepository
import org.koin.java.KoinJavaComponent.get

class ModelRepositoryImpl(
    private val apiService: ApiService = get(ApiService::class.java),
    private val modelDao: ModelDao = get(ModelDao::class.java)
) : ModelRepository {
    override suspend fun getAvailableModels(): List<Model> {
        var modelsEntity = modelDao.getModels()

        // If we have models in the database, we return them
        if (modelsEntity.isNotEmpty())
            return modelsEntity.map { Model(id = it.id, name = it.name, index = it.index) }

        modelsEntity = apiService.getModels().map { ModelEntity(id = it.id, name = it.name, index = it.index) }
        modelDao.insertModels(modelsEntity)

        return modelsEntity.map { Model(id = it.id, name = it.name, index = it.index) }
    }
}