package ir.partsoftware.programmingquote.network.author

import com.squareup.moshi.JsonClass
import ir.partsoftware.programmingquote.database.entity.AuthorEntity

@JsonClass(generateAdapter = true)
data class Author(
    val id: String,
    val name: String,
    val extract: String?,
    val infoUrl: String?,
    val image: String?,
    val quoteCount: Int?
){
    fun toAuthorEntity()=AuthorEntity(id=this.id,name=this.name,extract=this
        .extract,infoUrl = this.infoUrl,image=this.image,quoteCount=this.quoteCount)
}