package com.example.my_quotes.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my_quotes.R;
import com.example.my_quotes.database.QuotesDatabase;
import com.example.my_quotes.entities.Quote;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CreateQuotesActivity extends AppCompatActivity {

    private EditText inputQuoteTitle, inputQuoteSubtitle, inputQuoteText;
    private TextView textDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quotes);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onBackPressed(); }});

        inputQuoteTitle = findViewById(R.id.inputQuoteText);
        inputQuoteSubtitle = findViewById(R.id.inputQuoteAuthor);
        inputQuoteText = findViewById(R.id.inputQuoteSource);

        textDateTime = findViewById(R.id.textDateTime);

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQuote();
            }
        });
    }

    private void saveQuote() {
        if (inputQuoteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Вы не вписали цитату!", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputQuoteSubtitle.getText().toString().trim().isEmpty()
        && inputQuoteText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Вы не вписали автора!", Toast.LENGTH_SHORT).show();
            return;
        }

        final Quote quote = new Quote();
        quote.setTitle(inputQuoteTitle.getText().toString());
        quote.setSubtitle(inputQuoteSubtitle.getText().toString());
        quote.setQuoteText(inputQuoteText.getText().toString());
        quote.setDateTime(textDateTime.getText().toString());

        @SuppressLint("StaticFieldLeak")
        class SaveQuoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                QuotesDatabase.getDatabase(getApplicationContext()).quoteDao().insertQuote(quote);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new SaveQuoteTask().execute();
    }
}