package ir.partsoftware.programmingquote.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "authors")
data class AuthorEntity(
    @PrimaryKey val id: String,
    val name: String,
    val extract: String?,
    val infoUrl: String?,
    val image: String?,
    val quoteCount: Int?
)
