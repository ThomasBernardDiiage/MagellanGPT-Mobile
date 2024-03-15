package fr.group5.magellangpt.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.group5.magellangpt.data.local.entities.ModelEntity

@Dao
interface ModelDao {
    @Query("SELECT * FROM modelentity")
    suspend fun getModels(): List<ModelEntity>

    @Query("SELECT * FROM modelentity WHERE id = :id")
    suspend fun getModel(id : String): ModelEntity


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModels(models: List<ModelEntity>)
}