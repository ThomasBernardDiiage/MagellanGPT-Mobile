package fr.group5.magellangpt.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import fr.group5.magellangpt.data.local.entities.ConversationEntity

@Dao
interface ConversationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversations(conversations : List<ConversationEntity>)
}