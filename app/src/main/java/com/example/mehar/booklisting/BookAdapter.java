package com.example.mehar.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Book currentListItem = getItem(position);
        TextView bookTitle = (TextView) listItemView.findViewById(R.id.book_title);
        bookTitle.setText(currentListItem.getBookTitle());
        String[] authorsArray = currentListItem.getAuthors();
        String authorsText = authorsArray[0];
        if (authorsArray.length > 1) {
            int i;
            for (i = 1; i < authorsArray.length; i++) {
                authorsText = authorsText + ", ";
                authorsText = authorsText + authorsArray[i];
            }
        }
        TextView authors = (TextView) listItemView.findViewById(R.id.authors);
        authors.setText(authorsText);
        TextView averageRating = (TextView) listItemView.findViewById(R.id.average_rating);
        averageRating.setText(currentListItem.getAverageRating());
        return listItemView;
    }
}