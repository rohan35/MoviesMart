package com.example.admin.moviesmart.model;

/**
 * Created by admin on 1/7/2017.
 */

public class MovieTrailer {
    String mtrailerName;
    String mId;
    private String temp_path = "http://image.tmdb.org/t/p/w185/";

    private String mImagePath;


    public MovieTrailer(String trailerName, String id, String imagePath) {
        if (imagePath.contains("ImageFolder")) {
            mImagePath = imagePath;
        } else {
            mImagePath = temp_path.concat(imagePath);
        }

        mtrailerName = trailerName;
        mId = id;
    }

    public String getMtrailerName() {
        return mtrailerName;
    }

    public String getmId() {
        return mId;
    }

    public String getImagePath() {
        return mImagePath;
    }
}
