package com.example.mehar.booklisting;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    private String mBookTitle;
    private String[] mAuthors;
    private String mBuyLink;
    private String mAverageRating;
    private String mISBN10;
    private String mISBN13;
    private String mPageCount;

    public Book(String bookTitle, String authors[], String buyLink, String averageRating, String ISBN10, String ISBN13, String pageCount) {
        mBookTitle = bookTitle;
        mAuthors = authors;
        mBuyLink = buyLink;
        mAverageRating = averageRating;
        mISBN10 = ISBN10;
        mISBN13 = ISBN13;
        mPageCount = pageCount;
    }

    public Book(Parcel in) {
        mBookTitle = in.readString();
        mAuthors = in.createStringArray();
        mBuyLink = in.readString();
        mAverageRating = in.readString();
        mISBN10 = in.readString();
        mISBN13 = in.readString();
        mPageCount = in.readString();
    }

    public String getBookTitle() {
        return mBookTitle;
    }

    public String[] getAuthors() {
        return mAuthors;
    }

    public String getBuyLink() {
        return mBuyLink;
    }

    public String getAverageRating() {
        return mAverageRating;
    }

    public String getISBN10() {
        return mISBN10;
    }

    public String getISBN13() {
        return mISBN13;
    }

    public String getPageCount() {
        return mPageCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mBookTitle);
        dest.writeStringArray(mAuthors);
        dest.writeString(mBuyLink);
        dest.writeString(mAverageRating);
        dest.writeString(mISBN10);
        dest.writeString(mISBN13);
        dest.writeString(mPageCount);
    }
}