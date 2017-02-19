package com.example.admin.moviesmart.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.moviesmart.MainActivity;
import com.example.admin.moviesmart.R;
import com.example.admin.moviesmart.data.MoviesContract;
import com.example.admin.moviesmart.data.MoviesDbHelper;
import com.example.admin.moviesmart.fetchers.FetchMovieDetails;
import com.example.admin.moviesmart.fetchers.OnResultsRetrieved;
import com.example.admin.moviesmart.model.MoviePoster;
import com.example.admin.moviesmart.model.MovieTrailer;
import com.example.admin.moviesmart.utils.NetworkUtils;
import com.example.admin.moviesmart.utils.TrailersRecyclerAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 12/30/2016.
 */

public class MovieDetails extends AppCompatActivity {
    //Instantiate views
    @BindView(R.id.original_title)
    TextView original_title;
    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.summary)
    TextView overview;
    @BindView(R.id.release_date)
    TextView releaseDate;
    @BindView(R.id.pb)
    ProgressBar pb;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.favourite)
    ImageButton fav_view;
    @BindView(R.id.no_trailers)
    TextView noTrailers;
    SQLiteDatabase mDb;//Database object

    //Create arraylist object of movieTrailers
    ArrayList<MovieTrailer> movieTrailers = new ArrayList<>();
    TrailersRecyclerAdapter adapter;
    String image_path = "";
    Cursor cursor;

    String id;//recieved from main Activity
    JSONArray reviews;

    JSONArray trailers;
    String selectQuery="";

    private String temp_path = "http://image.tmdb.org/t/p/w185/";
    String path;

    // Create Method for getting the results from Async task that returns the object of type FetchMovieDetails
    //Create  object of interface in this method
    public FetchMovieDetails getMovieDetails() {
        return new FetchMovieDetails(new OnResultsRetrieved() {


            @Override
            public void preRetrieving() {
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            public void getDetails(JSONObject details) {
                try {
                    //Set Textviews

                    original_title.setText(details.getString("original_title"));
                    overview.setText(details.getString("overview"));
                    releaseDate.setText(details.getString("release_date"));
                    rating.setText(Double.toString(details.getDouble("vote_average")));
                    pb.setVisibility(View.INVISIBLE);
                    //instantiate trailers and views
                    trailers = details.getJSONObject("videos").getJSONArray("results");
                    reviews = details.getJSONObject("reviews").getJSONArray("results");
                    if (trailers.length() == 0) {
                        noTrailers.setVisibility(View.VISIBLE);
                    } else {

                        for (int i = 0; i < trailers.length(); i++) {
                            //get all the trailers and store pass it to movieTrailer arraylist object


                            JSONObject j = (JSONObject) trailers.get(i);

                            movieTrailers.add(new MovieTrailer(j.getString("name"), j.getString("key"), details.getString("poster_path")));
                        }
                    }
                    adapter = new TrailersRecyclerAdapter(getApplication(), movieTrailers);
                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(horizontalLayoutManager);
                    rv.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void getMovies(ArrayList<MoviePoster> results) {

            }

            //Method to get the details of the ovie when users click favourite
            @Override
            public void getDetails2(JSONObject details) {
                try {
                    String name = details.getString("original_title");
                    String overView = details.getString("overview");
                    String release_date = details.getString("release_date");
                    Double mRating = Double.parseDouble(details.getString("vote_average"));


                    addDetails(name, overView, release_date, mRating, id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    trailers = details.getJSONObject("videos").getJSONArray("results");
                    reviews = details.getJSONObject("reviews").getJSONArray("results");
                    if (trailers.length() == 0) {
                        noTrailers.setVisibility(View.VISIBLE);
                        image_path = details.getString("poster_path");
                        addTrailers(null, image_path,null, id);
                    } else {

                        for (int i = 0; i < trailers.length(); i++) {

                            JSONObject j = (JSONObject) trailers.get(i);
                            image_path = details.getString("poster_path");

                            addTrailers(j.getString("key"), image_path, j.getString("name"), id);
                        }
                    }

                    Picasso.with(getApplicationContext())
                            .load(temp_path.concat(image_path))
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    // loaded bitmap is here (bitmap)
                                    path = saveToInternalStorage(bitmap);
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                    for (int i = 0; i < reviews.length(); i++) {

                        JSONObject reviews_object = null;
                        reviews_object = (JSONObject) reviews.get(i);
                        addReviews(reviews_object.getString("id"), reviews_object.getString("author"), reviews_object.getString("content"), id);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fav_view.setImageResource(R.drawable.saved);
                Toast.makeText(getApplicationContext(), "Marked Favourite", Toast.LENGTH_SHORT).show();


            }
        }, this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("ID");
        MoviesDbHelper movies_helper = new MoviesDbHelper(this);
        mDb = movies_helper.getWritableDatabase();


        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //check if internet is present then only get the results otherwise prompt a alert box

        try {
            if (checkSavedMovie()) {
                getSavedMovie();
            } else {
                if (!tryAgain()) {
                    new AlertDialog.Builder(this)
                            .setTitle("No Internet")
                            .setMessage("It seems that you don't have internet connection, please try again later!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    finish();
                                }
                            })
                            .show();
                } else
 

                {
                    getMovieDetails().fetchSpecificData(id);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void favourite(View v) {
        if (checkSavedMovie()) {
            removeMovie(id);
            fav_view.setImageResource(R.drawable.favourite);
            Toast.makeText(this, "Removed from Favourite", Toast.LENGTH_SHORT).show();
        } else {
            getMovieDetails().fetchSpecificData(id, "0");

            if (overview.getText().length() == 0 || releaseDate.getText().length() == 0 || original_title.getText().length() == 0 ||
                    rating.getText().length() == 0) {
                return;
            }
        }
    }

    //Method to save the details in DEtails table
    private long addDetails(String mName, String mOverView, String mRelease_date, Double mRating, String mId) {
        ContentValues cv = new ContentValues();
        cv.put(MoviesContract.MoviesData.COLUMN_MOVIE_ID, mId);
        cv.put(MoviesContract.MoviesData.COLUMN_MOVIE_NAME, mName);
        cv.put(MoviesContract.MoviesData.COLUMN_MOVIE_SUMMARY, mOverView);
        cv.put(MoviesContract.MoviesData.COLUMN_MOVIE_RELEASE_DATE, mRelease_date);
        cv.put(MoviesContract.MoviesData.COLUMN_MOVIE_RATING, mRating);
        return mDb.insert(MoviesContract.MoviesData.TABLE_NAME, null, cv);
    }

    //Method to save the value in Trailers table
    private long addTrailers(String videoId, String trailerImagePath, String trailerName, String mId) {
        ContentValues cv = new ContentValues();

        cv.put(MoviesContract.MoviesData.COLUMN_TRAILER_ID, mId);
        cv.put(MoviesContract.MoviesData.COLUMN_TRAILER_VIDEO_ID, videoId);
        cv.put(MoviesContract.MoviesData.COLUMN_TRAILER_IMAGE_PATH, trailerImagePath);

        cv.put(MoviesContract.MoviesData.COLUMN_TRAILER_NAME, trailerName);
        return mDb.insert(MoviesContract.MoviesData.TABLE_NAME_2, null, cv);

    }

    //Method to save the value in reviews table
    private long addReviews(String reviewId, String reviewerName, String reviewDetails, String mId) {
        ContentValues cv = new ContentValues();

        cv.put(MoviesContract.MoviesData.COLUMN_Review_ID, mId);

        cv.put(MoviesContract.MoviesData.COLUMN_REVIEW_CONTENT_ID, reviewId);
        cv.put(MoviesContract.MoviesData.COLUMN_REVIEW_NAME, reviewerName);
        cv.put(MoviesContract.MoviesData.COLUMN_REVIEW_DETAIL, reviewDetails);
        return mDb.insert(MoviesContract.MoviesData.TABLE_NAME_3, null, cv);

    }

    private boolean removeMovie(String movieId) {
        return mDb.delete(MoviesContract.MoviesData.TABLE_NAME, MoviesContract.MoviesData.COLUMN_MOVIE_ID + "=" + movieId, null) > 0;
    }

    //Method to save the image offline in internal storage
    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageFolder
        File directory = cw.getDir("ImageFolder", Context.MODE_PRIVATE);
        // Create imageFolder
        File mypath = new File(directory, original_title.getText().toString());


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }


    //Method to check if movie is available in database
    public boolean checkSavedMovie() {

      selectQuery = "SELECT * FROM (select * from " + MoviesContract.MoviesData.TABLE_NAME + ") as a , (select * from " +
                MoviesContract.MoviesData.TABLE_NAME_2 + " group by " + MoviesContract.MoviesData.COLUMN_TRAILER_VIDEO_ID + ") as b  where a." + MoviesContract.MoviesData.COLUMN_MOVIE_ID +
                "= b." + MoviesContract.MoviesData.COLUMN_TRAILER_ID + " and a." +
                MoviesContract.MoviesData.COLUMN_MOVIE_ID + "='" + id + "\'";

        cursor = mDb.rawQuery(selectQuery, null);

        if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
            //cursor is empty
            return false;
        } else {
            return true;
        }


    }

    //Method to  get the results of movie that exist in database
    public void getSavedMovie() {
        fav_view.setImageResource(R.drawable.saved);


        original_title.setText(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesData.COLUMN_MOVIE_NAME)));
        overview.setText(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesData.COLUMN_MOVIE_SUMMARY)));
        releaseDate.setText(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesData.COLUMN_MOVIE_RELEASE_DATE)));
        rating.setText(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesData.COLUMN_MOVIE_RATING)));

        try {
            if(cursor.isNull(cursor.getColumnIndex(MoviesContract.MoviesData.COLUMN_TRAILER_VIDEO_ID)))
            {
                noTrailers.setVisibility(View.VISIBLE);
            }
            else
            {
            if (cursor.moveToFirst()) {
                do {
                    movieTrailers.add(new MovieTrailer(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesData.COLUMN_TRAILER_NAME)),
                            cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesData.COLUMN_TRAILER_VIDEO_ID)),
                            cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesData.COLUMN_TRAILER_IMAGE_PATH))));

                } while (cursor.moveToNext());
            }}

        } finally {
            cursor.close();
        }
        adapter = new TrailersRecyclerAdapter(getApplication(), movieTrailers);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(horizontalLayoutManager);
        rv.setAdapter(adapter);


    }

    //Method to pass id to reviews activity
    public void showReviews(View v) {
        Intent i = new Intent(MovieDetails.this, Reviews.class);

        Bundle b = new Bundle();
        if (checkSavedMovie()) {
            b.putString("ID", id);
            b.putInt("CHECK", 0);
        } else {
            b.putString("Array", reviews.toString());
            b.putInt("CHECK", 1);
        }
        i.putExtras(b);
        startActivity(i);
    }

    //method to check internet connectivity
    public boolean tryAgain() {
        if (!NetworkUtils.isNetworkConnected(this)) {
            return false;
        } else {


            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
