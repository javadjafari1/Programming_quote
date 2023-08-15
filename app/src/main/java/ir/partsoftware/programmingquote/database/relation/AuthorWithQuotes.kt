package ir.partsoftware.programmingquote.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import ir.partsoftware.programmingquote.database.author.AuthorEntity
import ir.partsoftware.programmingquote.database.quote.QuoteEntity

data class AuthorWithQuotes(
    @Embedded val authorEntity: AuthorEntity,
    @Relation(parentColumn = "id", entityColumn = "author")
    val quoteEntities: List<QuoteEntity>
)
