package ir.partsoftware.programmingquote.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.partsoftware.programmingquote.database.dao.AuthorDao
import ir.partsoftware.programmingquote.database.dao.QuoteDao
import ir.partsoftware.programmingquote.database.entity.AuthorEntity
import ir.partsoftware.programmingquote.database.entity.QuoteEntity

@Database(entities = [AuthorEntity::class,QuoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authorDao():AuthorDao
    abstract fun quoteDao():QuoteDao
}