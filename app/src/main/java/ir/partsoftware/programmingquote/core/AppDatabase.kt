package ir.partsoftware.programmingquote.core

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.partsoftware.programmingquote.database.author.AuthorDao
import ir.partsoftware.programmingquote.database.author.AuthorEntity
import ir.partsoftware.programmingquote.database.quote.QuoteDao
import ir.partsoftware.programmingquote.database.quote.QuoteEntity

@Database(
    entities = [AuthorEntity::class, QuoteEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authorDao(): AuthorDao
    abstract fun quoteDao(): QuoteDao
}