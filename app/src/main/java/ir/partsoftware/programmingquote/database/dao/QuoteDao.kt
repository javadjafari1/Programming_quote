package ir.partsoftware.programmingquote.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ir.partsoftware.programmingquote.database.entity.QuoteEntity
import ir.partsoftware.programmingquote.database.relation.QuoteWithAuthor
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<QuoteEntity>)

    @Update
    suspend fun updateQuote(quoteEntity: QuoteEntity)

    @Query("Select * from quotes")
    fun observeQuotes(): Flow<List<QuoteEntity>>

    @Query("Select * from quotes where id=:id")
    fun observeQuoteWithAuthor(id: String): Flow<QuoteWithAuthor>
}