package ir.partsoftware.programmingquote.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import ir.partsoftware.programmingquote.database.author.AuthorEntity
import ir.partsoftware.programmingquote.database.quote.QuoteEntity

data class QuoteWithAuthor(
    @Embedded val quoteEntity: QuoteEntity,
    @Relation(parentColumn = "author", entityColumn = "id")
    val authorEntity: AuthorEntity
)
