package com.example.mehar.booklisting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        TextView detailsTitle = findViewById(R.id.details_title);
        detailsTitle.setText(extras.getString("bookTitle"));
        TextView detailsAuthors = findViewById(R.id.details_authors);
        String[] authorsArray = extras.getStringArray("authors");
        String authorsText = authorsArray[0];
        if (authorsArray.length > 1) {
            int i;
            for (i = 1; i < authorsArray.length; i++) {
                authorsText = authorsText + ", ";
                authorsText = authorsText + authorsArray[i];
            }
        }
        detailsAuthors.setText(authorsText);
        TextView detailsAverageRating = findViewById(R.id.details_average_rating);
        detailsAverageRating.setText(extras.getString("avgRating"));

        TextView ISBN10 = findViewById(R.id.isbn_ten);
        ISBN10.setText(extras.getString("ISBN10"));

        TextView ISBN13 = findViewById(R.id.isbn_thirteen);
        ISBN13.setText(extras.getString("ISBN13"));

        TextView pageCount = findViewById(R.id.page_count);
        pageCount.setText(extras.getString("pageCount"));

        Button buyButton = findViewById(R.id.buy_button);
        final String buyLink = extras.getString("buyLink");
        if (buyLink == null) {
            buyButton.setText(R.string.not_available);
        } else {
            buyButton.setText(R.string.available);
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent buttonIntent = new Intent(Intent.ACTION_VIEW);
                    buttonIntent.setData(Uri.parse(buyLink));
                    startActivity(buttonIntent);
                }
            });
        }
    }
}
