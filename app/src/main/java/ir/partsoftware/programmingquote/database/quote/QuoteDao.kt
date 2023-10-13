package ir.partsoftware.programmingquote.database.quote

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ir.partsoftware.programmingquote.database.relation.QuoteWithAuthor
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<QuoteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Update
    suspend fun updateQuote(quote: QuoteEntity)

    @Query("select * from quotes")
    fun observeQuotes(): Flow<List<QuoteEntity>>

    @Transaction
    @Query("select * from quotes where id=:id")
    fun observeQuoteWithAuthor(id: String): Flow<QuoteWithAuthor>
}