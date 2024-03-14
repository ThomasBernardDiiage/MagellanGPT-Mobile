package fr.group5.magellangpt.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.group5.magellangpt.data.local.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messageentity ORDER BY date ASC")
    fun getMessages() : Flow<List<MessageEntity>>

    @Insert
    suspend fun insertMessage(message : MessageEntity) : Long

    @Query("UPDATE messageentity SET content = :content WHERE id = :id")
    suspend fun updateMessageContent(id : Long, content : String)

    @Query("DELETE FROM messageentity WHERE id = :id")
    suspend fun deleteMessage(id: Long)
}