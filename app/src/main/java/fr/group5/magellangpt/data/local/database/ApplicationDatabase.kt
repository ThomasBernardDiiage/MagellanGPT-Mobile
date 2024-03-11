package fr.group5.magellangpt.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.group5.magellangpt.data.local.dao.MessageDao
import fr.group5.magellangpt.data.local.entities.MessageEntity

@Database(
    entities = [
        MessageEntity::class,
    ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}