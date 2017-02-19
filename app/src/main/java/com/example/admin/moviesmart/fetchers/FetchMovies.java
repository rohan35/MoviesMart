package com.example.admin.moviesmart.fetchers;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.moviesmart.model.MoviePoster;
import com.example.admin.moviesmart.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by admin on 12/30/2016.
 */

public class FetchMovies {
    ArrayList<MoviePoster> moviePoster = new ArrayList<MoviePoster>();
    OnResultsRetrieved onResultsRetrieved;
    Context context;

    //constructor for implementing interface in another classes
    public FetchMovies(OnResultsRetrieved onResultsRetrieved1, Context c) {
        this.context = c;
        this.moviePoster = new ArrayList<>();
        this.onResultsRetrieved = onResultsRetrieved1;
    }


    public void fetchAllMovies(final String params) {
        URL url = null;
        //check type of Movie
        if (params == "popular") {
            url = NetworkUtils.buildUrl("popular");
        } else {
            url = NetworkUtils.buildUrl("top_rated");
        }


        //get the data
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray results = null;
                        try {
                            results = response.getJSONArray("results");


                            for (int i = 0; i < results.length(); i++) {
                                final JSONObject details = results.getJSONObject(i);
                                moviePoster.add(new MoviePoster(details.getString("poster_path"), details.getString("id")));
                            }
//add to Arraylist
                            onResultsRetrieved.getMovies(moviePoster);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Volley.newRequestQueue(context).add(jsonObjectRequest);
        //passed the results through object of constructor


    }


}
