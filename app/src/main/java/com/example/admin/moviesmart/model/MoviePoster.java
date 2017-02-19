package com.example.admin.moviesmart.model;

/**
 * Created by admin on 12/30/2016.
 */

public class MoviePoster {
    private String temp_path = "http://image.tmdb.org/t/p/w185/";

    private String mImagePath;
    private String mId;

    public MoviePoster(String imagePath, String id)

    {
        if (imagePath.contains("ImageFolder")) {
            mImagePath = imagePath;
        } else {
            mImagePath = temp_path.concat(imagePath);
        }
        mId = id;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public String getId() {
        return mId;
    }
}
