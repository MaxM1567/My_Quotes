package com.example.my_quotes.listeners;

import com.example.my_quotes.entities.Quote;

public interface QuotesListener {
    void onQuoteClicked(Quote quote, int position);
}
