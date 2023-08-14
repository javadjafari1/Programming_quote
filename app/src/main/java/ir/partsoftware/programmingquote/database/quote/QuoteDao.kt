package ir.partsoftware.programmingquote.database.quote

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotes(quotes: List<QuoteEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: QuoteEntity)

    @Query("select * from quotes")
    fun getQuotes(): Flow<List<QuoteEntity>>

    @Query("select * from quotes where id=:id")
    fun getQuote(id: String): Flow<QuoteEntity>
}