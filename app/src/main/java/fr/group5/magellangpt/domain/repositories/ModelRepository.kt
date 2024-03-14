package fr.group5.magellangpt.domain.repositories

import fr.group5.magellangpt.domain.models.Model

interface ModelRepository {
    /**
     * Get available models
     * Exemple:
     * Gpt3-5, Gpt4
     */
    suspend fun getAvailableModels() : List<Model>
}