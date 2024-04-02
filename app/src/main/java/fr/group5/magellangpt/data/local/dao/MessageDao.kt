package fr.group5.magellangpt.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.group5.magellangpt.data.local.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messageentity")
    fun getMessages() : Flow<List<MessageEntity>>

    @Insert
    suspend fun insertMessage(message : MessageEntity) : Long

    @Query("DELETE FROM messageentity WHERE id = :messageId")
    suspend fun deleteMessage(messageId : Long)

    @Insert
    suspend fun insertMessages(messages : List<MessageEntity>)

    @Query("DELETE FROM messageentity")
    fun nuke()
}