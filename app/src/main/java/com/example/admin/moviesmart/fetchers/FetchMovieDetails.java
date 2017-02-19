package com.example.admin.moviesmart.fetchers;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.moviesmart.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;

/**
 * Created by admin on 12/30/2016.
 */

public class FetchMovieDetails {
    Context context;
    private OnResultsRetrieved retrievedResults;

    public FetchMovieDetails(OnResultsRetrieved retrievedResults, Context c) {
        this.context = c;
        this.retrievedResults = retrievedResults;
    }


    public void fetchSpecificData(String params, final String... i) {
        URL url = null;
        url = NetworkUtils.buildDetailsUrl(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (i.length > 0) {

                            retrievedResults.getDetails2(response);

                        } else {
                            retrievedResults.getDetails(response);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }


        );


        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }


}
