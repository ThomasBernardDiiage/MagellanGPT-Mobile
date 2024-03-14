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
    suspend fun insertMessage(message : MessageEntity)

    @Insert
    suspend fun insertMessages(messages : List<MessageEntity>)

    @Query("DELETE FROM messageentity")
    fun nuke()
}