package com.example.my_quotes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.my_quotes.entities.Quote;

import java.util.List;

@Dao
public interface QuoteDao {
    @Query("SELECT * FROM quotes ORDER BY id DESC")
    List<Quote> getAllQuotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuote(Quote quote);

    @Delete
    void deleteQuote(Quote quote);
}
