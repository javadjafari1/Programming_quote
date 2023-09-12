package ir.partsoftware.programmingquote.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ir.partsoftware.programmingquote.database.entity.AuthorEntity
import ir.partsoftware.programmingquote.database.relation.AuthorWithQuotes
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthors(authors:List<AuthorEntity>)

    @Update
    suspend fun updateAuthor(authorEntity: AuthorEntity)

    @Query("SELECT * FROM AUTHORS")
    fun observeAuthors():Flow<List<AuthorEntity>>

    @Transaction
    @Query("Select * from authors where id=:id")
    fun observeAuthorWithQuotes(id:String):Flow<AuthorWithQuotes>

}