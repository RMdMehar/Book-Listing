package com.example.mehar.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    private static final int BOOK_LOADER_ID = 1;
    private static String requestURL = "https://www.googleapis.com/books/v1/volumes?q=";
    LoaderManager loaderManager;
    ArrayList<Book> bookArrayList = new ArrayList<>();
    private String maxResults = "&maxResults=10";
    private BookAdapter adapter;
    private TextView emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            bookArrayList = savedInstanceState.getParcelableArrayList("key");
        }
        ListView bookListView = (ListView) findViewById(R.id.list);
        adapter = new BookAdapter(this, bookArrayList);
        emptyState = findViewById(R.id.empty_state_text);
        bookListView.setEmptyView(emptyState);
        bookListView.setAdapter(adapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book currentBook = adapter.getItem(position);
                String bookTitle = currentBook.getBookTitle();
                String[] authors = currentBook.getAuthors();
                String buyLink = currentBook.getBuyLink();
                String avgRating = currentBook.getAverageRating();
                String ISBN10 = currentBook.getISBN10();
                String ISBN13 = currentBook.getISBN13();
                String pageCount = currentBook.getPageCount();
                Bundle extras = new Bundle();
                extras.putString("bookTitle", bookTitle);
                extras.putStringArray("authors", authors);
                extras.putString("buyLink", buyLink);
                extras.putString("avgRating", avgRating);
                extras.putString("ISBN10", ISBN10);
                extras.putString("ISBN13", ISBN13);
                extras.putString("pageCount", pageCount);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if ((activeNetwork != null) && (activeNetwork.isConnected())) {
            emptyState.setText(R.string.search_hint);
        } else {
            emptyState.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookLoader(this, requestURL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        emptyState.setText(R.string.not_found);
        adapter.clear();
        if (books != null && !books.isEmpty()) {
            adapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchbar, menu);
        MenuItem item = menu.findItem(R.id.search_query);
        final SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String oldQuery = requestURL.substring(46);
                query = query + maxResults;
                loaderManager = getLoaderManager();
                if (oldQuery.equals("")) {
                    requestURL = requestURL + query;
                    loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
                } else {
                    requestURL = requestURL.replace(oldQuery, query);
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("key", bookArrayList);
    }
}