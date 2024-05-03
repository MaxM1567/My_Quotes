package com.example.my_quotes.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.my_quotes.dao.QuoteDao;
import com.example.my_quotes.entities.Quote;

@Database(entities = Quote.class, version = 1, exportSchema = false)
public abstract class QuotesDatabase extends RoomDatabase {
    private static QuotesDatabase quotesDatabase;
    public static synchronized QuotesDatabase getDatabase(Context context) {
        if (quotesDatabase == null) {
            Log.d("TEST", "aboba~Build new DATABASE");
            quotesDatabase = Room.databaseBuilder(context, QuotesDatabase.class, "quotes_db").build();
        }
        return quotesDatabase;
    }

    public abstract QuoteDao quoteDao();
}
