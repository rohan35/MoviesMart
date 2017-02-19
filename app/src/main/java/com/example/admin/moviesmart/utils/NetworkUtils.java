package com.example.admin.moviesmart.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by admin on 12/30/2016.
 */

public class NetworkUtils {
    private static final String Api_key = "2fbbdbb959d641188787ae098f7da1c7";
    private static final String url = "http://api.themoviedb.org/3/movie";

    // Create method to build the url
    public static URL buildUrl(String type) {
        Uri buildUri;

        buildUri = Uri.parse(url).buildUpon().appendPath(type).appendQueryParameter("api_key", Api_key)
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
        return url;

    }

    public static URL buildDetailsUrl(String id) {
        Uri buildUri;

        buildUri = Uri.parse(url).buildUpon().appendPath(id).appendQueryParameter("api_key", Api_key)
                .appendQueryParameter("append_to_response", "videos,reviews").build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
        return url;

    }


    //Check Internet connectivity
    public static boolean isNetworkConnected(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
