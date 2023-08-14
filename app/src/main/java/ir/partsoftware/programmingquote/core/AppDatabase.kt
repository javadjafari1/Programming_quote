package ir.partsoftware.programmingquote.core

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.partsoftware.programmingquote.database.author.AuthorDao
import ir.partsoftware.programmingquote.database.author.AuthorEntity

@Database(
    entities = [AuthorEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authorDao(): AuthorDao
}