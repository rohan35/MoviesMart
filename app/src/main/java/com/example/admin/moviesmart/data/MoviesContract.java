package com.example.admin.moviesmart.data;

import android.provider.BaseColumns;

/**
 * Created by admin on 1/9/2017.
 */

public class MoviesContract {
    private MoviesContract() {

    }

    public static final class MoviesData implements BaseColumns {
        public static final String TABLE_NAME = "details";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_NAME = "movie_name";
        public static final String COLUMN_MOVIE_SUMMARY = "summary";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_RATING = "rating";

        public static final String TABLE_NAME_2 = "trailers";
        public static final String COLUMN_TRAILER_VIDEO_ID = "trailer_id";
        public static final String COLUMN_TRAILER_ID = "id";
        public static final String COLUMN_TRAILER_IMAGE_PATH = "image_path";
        public static final String COLUMN_TRAILER_NAME = "trailer_name";

        public static final String TABLE_NAME_3 = "reviews";
        public static final String COLUMN_REVIEW_CONTENT_ID = "content_id";
        public static final String COLUMN_Review_ID = "id";
        public static final String COLUMN_REVIEW_NAME = "reviewer_name";
        public static final String COLUMN_REVIEW_DETAIL = "review";


    }
}
