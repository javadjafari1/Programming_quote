package ir.partsoftware.programmingquote.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import ir.partsoftware.programmingquote.database.author.AuthorEntity
import ir.partsoftware.programmingquote.database.quote.QuoteEntity

data class QuoteWithAuthor(
    @Embedded val quote: QuoteEntity,
    @Relation(
        parentColumn = "authorId",
        entityColumn = "id"
    )
    val author: AuthorEntity
)
