package com.example.my_quotes.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.my_quotes.R;
import com.example.my_quotes.adapters.QuotesAdapter;
import com.example.my_quotes.database.QuotesDatabase;
import com.example.my_quotes.entities.Quote;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_QUOTE = 1;

    private RecyclerView quotesRecyclerView;
    private List<Quote> quoteList;
    private QuotesAdapter quotesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageAddQuoteMain = findViewById(R.id.imageAddQuoteMain);
        imageAddQuoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateQuotesActivity.class),
                        REQUEST_CODE_ADD_QUOTE
                );
            }
        });

        quotesRecyclerView = findViewById(R.id.quotesRecyclerView);
        quotesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        );
        quoteList = new ArrayList<>();
        quotesAdapter = new QuotesAdapter(quoteList);
        quotesRecyclerView.setAdapter(quotesAdapter);

        getQuotes();
    }

    private void getQuotes() {
        @SuppressLint("StaticFieldLeak")
        class GetQuotesTask extends AsyncTask<Void, Void, List<Quote>> {
            @Override
            protected List<Quote> doInBackground(Void... voids) {
                return QuotesDatabase.getDatabase(getApplicationContext()).quoteDao().getAllQuotes();
            }

            @Override
            protected void onPostExecute(List<Quote> quotes) {
                super.onPostExecute(quotes);

                if (quoteList.isEmpty()) {
                    quoteList.addAll(quotes);
                    quotesAdapter.notifyDataSetChanged();
                } else {
                    quoteList.add(0, quotes.get(0));
                    quotesAdapter.notifyItemInserted(0);
                }
                quotesRecyclerView.smoothScrollToPosition(0);
            }
        }

        new GetQuotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_QUOTE && resultCode == RESULT_OK) {
            getQuotes();
        }
    }
}