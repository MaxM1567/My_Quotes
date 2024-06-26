package com.example.my_quotes.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.my_quotes.R;
import com.example.my_quotes.adapters.QuotesAdapter;
import com.example.my_quotes.database.QuotesDatabase;
import com.example.my_quotes.entities.Quote;
import com.example.my_quotes.listeners.QuotesListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements QuotesListener {

    public static final int REQUEST_CODE_ADD_QUOTE = 1;
    public static final int REQUEST_CODE_UPDATE_QUOTE = 2;
    public static final int REQUEST_CODE_SHOW_QUOTES = 3;

    private RecyclerView quotesRecyclerView;
    private List<Quote> quoteList;
    private QuotesAdapter quotesAdapter;

    private int quoteClickedPosition = -1;

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
        quotesAdapter = new QuotesAdapter(quoteList, this);
        quotesRecyclerView.setAdapter(quotesAdapter);

        getQuotes(REQUEST_CODE_SHOW_QUOTES, false);

        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quotesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!quoteList.isEmpty()) {
                    quotesAdapter.searchQuotes(s.toString());
                }
            }
        });
    }

    @Override
    public void onQuoteClicked(Quote quote, int position) {
        quoteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateQuotesActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("quote", quote);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_QUOTE);
    }

    private void getQuotes(final int requestCode, final boolean isQuoteDeleted) {
        @SuppressLint("StaticFieldLeak")
        class GetQuotesTask extends AsyncTask<Void, Void, List<Quote>> {
            @Override
            protected List<Quote> doInBackground(Void... voids) {
                return QuotesDatabase.getDatabase(getApplicationContext()).quoteDao().getAllQuotes();
            }

            @Override
            protected void onPostExecute(List<Quote> quotes) {
                super.onPostExecute(quotes);

                if (requestCode == REQUEST_CODE_SHOW_QUOTES) {
                    quoteList.addAll(quotes);
                    quotesAdapter.notifyDataSetChanged();

                } else if (requestCode == REQUEST_CODE_ADD_QUOTE) {
                    quoteList.add(0, quotes.get(0));
                    quotesAdapter.notifyItemInserted(0);
                    quotesRecyclerView.smoothScrollToPosition(0);

                } else if (requestCode == REQUEST_CODE_UPDATE_QUOTE) {
                    quoteList.remove(quoteClickedPosition);

                    if (isQuoteDeleted) {
                        quotesAdapter.notifyItemRemoved(quoteClickedPosition);
                    } else {
                        quoteList.add(quoteClickedPosition, quotes.get(quoteClickedPosition));
                        quotesAdapter.notifyItemChanged(quoteClickedPosition);
                    }
                }
            }
        }

        new GetQuotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_QUOTE && resultCode == RESULT_OK) {
            getQuotes(REQUEST_CODE_ADD_QUOTE, false);
        } else if (requestCode == REQUEST_CODE_UPDATE_QUOTE && resultCode == RESULT_OK) {
            if (data != null) {
                getQuotes(REQUEST_CODE_UPDATE_QUOTE, data.getBooleanExtra("isQuoteDeleted", false));
            }
        }
    }
}