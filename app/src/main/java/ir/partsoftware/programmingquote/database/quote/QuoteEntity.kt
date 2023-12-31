package ir.partsoftware.programmingquote.database.quote

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotes")
data class QuoteEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val authorId: String,
)
