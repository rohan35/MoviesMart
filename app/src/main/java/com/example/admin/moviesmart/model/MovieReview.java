package com.example.admin.moviesmart.model;

/**
 * Created by admin on 1/8/2017.
 */

public class MovieReview {
    String mReview;
    String mAuthor;

    public MovieReview(String review, String author) {
        mReview = review;
        mAuthor = author;
    }

    public String getmReview() {
        return mReview;
    }

    public String getmAuthor() {
        return mAuthor;
    }
}
