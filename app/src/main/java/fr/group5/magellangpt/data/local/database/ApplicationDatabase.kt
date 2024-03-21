package fr.group5.magellangpt.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.group5.magellangpt.data.local.dao.ConversationDao
import fr.group5.magellangpt.data.local.dao.MessageDao
import fr.group5.magellangpt.data.local.dao.ModelDao
import fr.group5.magellangpt.data.local.entities.ConversationEntity
import fr.group5.magellangpt.data.local.entities.MessageEntity
import fr.group5.magellangpt.data.local.entities.ModelEntity

@Database(
    entities = [
        MessageEntity::class,
        ConversationEntity::class,
        ModelEntity::class
    ],
    version = 1
)
@TypeConverters(DateConverter::class, ArrayConverter::class)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    abstract fun conversationDao(): ConversationDao

    abstract fun modelDao(): ModelDao
}