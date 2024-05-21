package com.example.my_quotes.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_quotes.R;
import com.example.my_quotes.entities.Quote;
import com.example.my_quotes.listeners.QuotesListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.QuoteViewHolder> {
    private List<Quote> quotes;
    private QuotesListener quotesListener;
    private Timer timer;
    private List<Quote> quoteSource;

    public QuotesAdapter(List<Quote> quotes, QuotesListener quotesListener) {
        this.quotes = quotes;
        this.quotesListener = quotesListener;
        quoteSource = quotes;
    }

    @NonNull
    @Override
    public QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_quote,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteViewHolder holder, int position) {
        holder.setQuote(quotes.get(position));
        holder.layoutQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    quotesListener.onQuoteClicked(quotes.get(adapterPosition), adapterPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class QuoteViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textSubtitle, textDateTime;
        LinearLayout layoutQuote;
        View categoryIndicator;

        QuoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubtitle = itemView.findViewById(R.id.textAuthor);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            categoryIndicator = itemView.findViewById(R.id.categoryIndicator);
            layoutQuote = itemView.findViewById(R.id.layoutQuote);
        }

        void setQuote(Quote quote) {
            textTitle.setText(quote.getTitle());
            if (quote.getSubtitle().trim().isEmpty()) {
                textSubtitle.setVisibility(View.GONE);
            } else {
                textSubtitle.setText(quote.getSubtitle());
            }
            textDateTime.setText(quote.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) categoryIndicator.getBackground();
            if (quote.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(quote.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#2a313f"));
            }
        }
    }

    public void searchQuotes(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    quotes = quoteSource;
                } else {
                    ArrayList<Quote> temp = new ArrayList<>();
                    for (Quote quote : quoteSource) {
                        if (quote.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                || quote.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                || quote.getQuoteText().toLowerCase().contains(searchKeyword.toLowerCase())) {
                            temp.add(quote);
                        }
                    }
                    quotes = temp;
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
