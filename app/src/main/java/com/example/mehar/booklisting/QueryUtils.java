package com.example.mehar.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    public static List<Book> extractBooks(String requestURL) {
        URL url = null;
        try {
            url = new URL(requestURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Custom Log: Error in creating URL");
        }
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Custom Log: Problem making HTTP request");
        }
        return extractFeatureFromJSON(jsonResponse);
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Custom Log: Error response code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Custom Log: Problem retrieving the book JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Book> extractFeatureFromJSON(String bookJSON) {
        ArrayList<Book> books = new ArrayList<>();
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }
        try {
            int i, j, k;
            JSONObject object = new JSONObject(bookJSON);
            JSONArray items = object.getJSONArray("items");
            for (i = 0; i < items.length(); i++) {
                JSONObject currentItem = items.getJSONObject(i);
                JSONObject currentVolumeInfo = currentItem.getJSONObject("volumeInfo");
                String bookTitle = currentVolumeInfo.getString("title");
                JSONArray authorsArray = currentVolumeInfo.getJSONArray("authors");
                String[] authors = new String[authorsArray.length()];
                for (j = 0; j < authors.length; j++) {
                    authors[j] = authorsArray.getString(j);
                }
                String averageRating;
                try {
                    averageRating = currentVolumeInfo.getString("averageRating");
                } catch (JSONException e) {
                    averageRating = "N/A";
                }
                String buyLink;
                try {
                    buyLink = currentItem.getJSONObject("saleInfo").getString("buyLink");
                } catch (JSONException e) {
                    buyLink = null;
                }
                JSONArray industryIdentifiers = currentVolumeInfo.getJSONArray("industryIdentifiers");
                String ISBN10 = new String("");
                String ISBN13 = new String("");
                JSONObject obj;
                for (k = 0; k < industryIdentifiers.length(); k++) {
                    obj = industryIdentifiers.getJSONObject(k);
                    switch (obj.getString("type")) {
                        case "ISBN_10":
                            ISBN10 = obj.getString("identifier");
                            break;

                        case "ISBN_13":
                            ISBN13 = obj.getString("identifier");
                            break;
                    }
                }
                if (ISBN10.equals("")) {
                    ISBN10 = "N/A";
                }
                if (ISBN13.equals("")) {
                    ISBN13 = "N/A";
                }
                String pageCount;
                try {
                    pageCount = currentVolumeInfo.getString("pageCount");
                } catch (JSONException e) {
                    pageCount = "N/A";
                }
                books.add(new Book(bookTitle, authors, buyLink, averageRating, ISBN10, ISBN13, pageCount));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Custom Log: Problem parsing the book JSON results", e);
        }
        return books;
    }
}