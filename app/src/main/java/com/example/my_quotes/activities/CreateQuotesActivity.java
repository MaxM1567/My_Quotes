package com.example.my_quotes.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.my_quotes.R;
import com.example.my_quotes.database.QuotesDatabase;
import com.example.my_quotes.entities.Quote;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CreateQuotesActivity extends AppCompatActivity {

    private EditText inputQuoteTitle, inputQuoteSubtitle, inputQuoteText;
    private TextView textDateTime;
    private View viewSubtitleIndicator;
    private TextView textWebSourceURL;
    private LinearLayout layoutWebURL;

    private String selectedQuoteCategory;
    private AlertDialog dialogAddSourceURL;
    private AlertDialog dialogDeleteQuote;

    private Quote alreadyAvailabQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quote);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        inputQuoteTitle = findViewById(R.id.inputQuoteText);
        inputQuoteSubtitle = findViewById(R.id.inputQuoteAuthor);
        inputQuoteText = findViewById(R.id.inputQuoteSource);

        textDateTime = findViewById(R.id.textDateTime);

        textDateTime.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", Locale.getDefault()).format(new Date()));

        viewSubtitleIndicator = findViewById(R.id.viewAuthorIndicator);

        textWebSourceURL = findViewById(R.id.textSourceURL);
        layoutWebURL = findViewById(R.id.layoutSourceURL);

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQuote();
            }
        });

        selectedQuoteCategory = "#2a313f";

        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailabQuote = (Quote) getIntent().getSerializableExtra("quote");
            setViewOrUpdateQuote();
        }

        findViewById(R.id.imageRemoveSourceURL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textWebSourceURL.setText(null);
                layoutWebURL.setVisibility(View.GONE);
            }
        });

        initMiscellaneous();
        setSubTitleIndicator();
    }

    private void setViewOrUpdateQuote() {
        inputQuoteTitle.setText(alreadyAvailabQuote.getTitle());
        inputQuoteSubtitle.setText(alreadyAvailabQuote.getSubtitle());
        inputQuoteText.setText(alreadyAvailabQuote.getQuoteText());
        textDateTime.setText(alreadyAvailabQuote.getDateTime());

        if (alreadyAvailabQuote.getWebLink() != null && !alreadyAvailabQuote.getWebLink().trim().isEmpty()) {
            textWebSourceURL.setText(alreadyAvailabQuote.getWebLink());
            layoutWebURL.setVisibility(View.VISIBLE);
        }
    }

    private void saveQuote() {
        if (inputQuoteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Вы не вписали цитату!", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputQuoteSubtitle.getText().toString().trim().isEmpty() && inputQuoteText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Вы не вписали автора!", Toast.LENGTH_SHORT).show();
            return;
        }

        final Quote quote = new Quote();
        quote.setTitle(inputQuoteTitle.getText().toString());
        quote.setSubtitle(inputQuoteSubtitle.getText().toString());
        quote.setQuoteText(inputQuoteText.getText().toString());
        quote.setDateTime(textDateTime.getText().toString());
        quote.setColor(selectedQuoteCategory);

        if (layoutWebURL.getVisibility() == View.VISIBLE) {
            quote.setWebLink(textWebSourceURL.getText().toString());
        }

        if (alreadyAvailabQuote != null) {
            quote.setId(alreadyAvailabQuote.getId());
        }

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

    private void initMiscellaneous() {
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imageColor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor6 = layoutMiscellaneous.findViewById(R.id.imageColor6);
        final ImageView imageColor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);

        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedQuoteCategory = "#2a313f";
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);

                setSubTitleIndicator();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedQuoteCategory = "#FDBE3B";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);

                setSubTitleIndicator();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedQuoteCategory = "#FF6600";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(R.drawable.ic_done);

                setSubTitleIndicator();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedQuoteCategory = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);

                setSubTitleIndicator();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedQuoteCategory = "#3A52Fc";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);

                setSubTitleIndicator();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedQuoteCategory = "#4E9F3D";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done);
                imageColor6.setImageResource(0);

                setSubTitleIndicator();
            }
        });

        if (alreadyAvailabQuote != null && alreadyAvailabQuote.getColor() != null && !alreadyAvailabQuote.getColor().trim().isEmpty()) {
            switch (alreadyAvailabQuote.getColor()) {
                case "#FDBE3B":
                    layoutMiscellaneous.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#FF6600":
                    layoutMiscellaneous.findViewById(R.id.viewColor6).performClick();
                    break;
                case "#FF4842":
                    layoutMiscellaneous.findViewById(R.id.viewColor3).performClick();
                    break;
                case "#3A52Fc":
                    layoutMiscellaneous.findViewById(R.id.viewColor4).performClick();
                    break;
                case "#4E9F3D":
                    layoutMiscellaneous.findViewById(R.id.viewColor5).performClick();
                    break;
            }
        }

        layoutMiscellaneous.findViewById(R.id.layoutAddSourceUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showAddURLSourceDialog();
            }
        });

        if (alreadyAvailabQuote != null) {
            layoutMiscellaneous.findViewById(R.id.layoutDeleteQuote).setVisibility(View.VISIBLE);
            layoutMiscellaneous.findViewById(R.id.layoutDeleteQuote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteDialog();
                }
            });
        }
    }

    private void showDeleteDialog() {
        if (dialogDeleteQuote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateQuotesActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_qoute,
                    (ViewGroup) findViewById(R.id.layoutDeleteQuoteContainer)
            );
            builder.setView(view);
            dialogDeleteQuote = builder.create();

            if (dialogDeleteQuote.getWindow() != null) {
                dialogDeleteQuote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteQuote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    @SuppressLint("StaticFieldLeak")
                    class DeleteQuoteTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            QuotesDatabase.getDatabase(getApplicationContext()).quoteDao()
                                    .deleteQuote(alreadyAvailabQuote);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void unused) {
                            super.onPostExecute(unused);
                            Intent intent = new Intent();
                            intent.putExtra("isQuoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                    new DeleteQuoteTask().execute();

                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDeleteQuote.dismiss();
                }
            });
        }
        dialogDeleteQuote.show();
    }

    private void setSubTitleIndicator() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedQuoteCategory));
    }

    private void showAddURLSourceDialog() {
        if (dialogAddSourceURL == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateQuotesActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_add_url_source, (ViewGroup) findViewById(R.id.layoutAddUrlSourceContainer));

            builder.setView(view);
            dialogAddSourceURL = builder.create();
            if (dialogAddSourceURL.getWindow() != null) {
                dialogAddSourceURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputSourceURL = view.findViewById(R.id.inputSourceURL);
            inputSourceURL.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (inputSourceURL.getText().toString().trim().isEmpty()) {
                        Toast.makeText(CreateQuotesActivity.this, "Укажите ссылку!", Toast.LENGTH_SHORT).show();
                    } else if (!Patterns.WEB_URL.matcher(inputSourceURL.getText().toString()).matches()) {
                        Toast.makeText(CreateQuotesActivity.this, "Укажите корректную ссылку!", Toast.LENGTH_SHORT).show();
                    } else {
                        textWebSourceURL.setText(inputSourceURL.getText().toString());
                        layoutWebURL.setVisibility(View.VISIBLE);
                        dialogAddSourceURL.dismiss();
                    }
                }
            });

            view.findViewById((R.id.textCancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogAddSourceURL.dismiss();
                }
            });
        }

        dialogAddSourceURL.show();
    }
}