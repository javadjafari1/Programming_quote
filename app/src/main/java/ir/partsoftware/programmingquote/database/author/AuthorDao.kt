package ir.partsoftware.programmingquote.database.author

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ir.partsoftware.programmingquote.database.relation.AuthorWithQuotes
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthors(authors: List<AuthorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthor(author: AuthorEntity)

    @Query("select * from authors")
    fun observeAuthors(): Flow<List<AuthorEntity>>

    @Update
    suspend fun updateAuthor(author: AuthorEntity)

    @Transaction
    @Query("select * from authors where id=:id limit 1")
    fun observeAuthorWithQuotes(id: String): Flow<AuthorWithQuotes>

}