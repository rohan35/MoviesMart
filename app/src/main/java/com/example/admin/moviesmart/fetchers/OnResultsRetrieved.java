package com.example.admin.moviesmart.fetchers;

import com.example.admin.moviesmart.model.MoviePoster;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 12/30/2016.
 */
//Interface to handle methods for Async task
public interface OnResultsRetrieved {
    void preRetrieving();

    void getDetails(JSONObject details);

    void getMovies(ArrayList<MoviePoster> results);

    void getDetails2(JSONObject details);

}
