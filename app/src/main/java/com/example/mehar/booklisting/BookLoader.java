package com.example.mehar.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private String requestURL;

    public BookLoader(Context context, String url) {
        super(context);
        requestURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (requestURL == null) {
            return null;
        }
        return QueryUtils.extractBooks(requestURL);
    }
}