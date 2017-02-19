package com.example.admin.moviesmart;

import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.admin.moviesmart.activities.MovieDetails;
import com.example.admin.moviesmart.data.MoviesContract;
import com.example.admin.moviesmart.data.MoviesDbHelper;
import com.example.admin.moviesmart.fetchers.FetchMovies;
import com.example.admin.moviesmart.fetchers.OnResultsRetrieved;
import com.example.admin.moviesmart.model.MoviePoster;
import com.example.admin.moviesmart.utils.MovieRecyclerAdapter;
import com.example.admin.moviesmart.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    //instantiate Views
    @BindView(R.id.rv_movies)
    RecyclerView rv;
    @BindView(R.id.tv_no_connection)
    TextView tv_no_Connection;
    @BindView(R.id.no_favourite)
    TextView no_favourite;
    //Create Arraylist of class Movie Poster
    ArrayList<MoviePoster> results = new ArrayList<>();
    MovieRecyclerAdapter adapter;
    //instantiate grid layout manager for grid view
    GridLayoutManager layoutManager;
    String type = "popular";//type that we want to retreive
    SQLiteDatabase mDb; //Database obect

    int sort_id;

    // Create Method for getting the results from Async task that returns the object of type FetchMovies
    //Create  object of interface in this method
    public FetchMovies getMovies() {
        return new FetchMovies(new OnResultsRetrieved() {
//Override All the methods of the interface

            @Override
            public void preRetrieving() {
            }

            @Override
            public void getDetails(JSONObject details) {


            }

            // Handle the results that are retrieved from FetchMovies
            @Override
            public void getMovies(ArrayList<MoviePoster> results1) {
                results.addAll(results1);
                adapter = new MovieRecyclerAdapter(MainActivity.this, results);
                rv.setAdapter(adapter);
                tv_no_Connection.setVisibility(View.GONE);


            }

            @Override
            public void getDetails2(JSONObject details) {

            }
        }, this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            sort_id = savedInstanceState.getInt("sort_type");
            type = savedInstanceState.getString("type");
        }
        MoviesDbHelper movies_helper = new MoviesDbHelper(this);
        mDb = movies_helper.getReadableDatabase();

        // Give value to layout manager and then to recyclerview
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            // on a large screen device ...
            layoutManager = new GridLayoutManager(MainActivity.this, 3);

        } else {
            layoutManager = new GridLayoutManager(MainActivity.this, 2);
        }
        rv.setHasFixedSize(true);
        rv.setLayoutManager(layoutManager);
        if (type == "fav") {
            getSavedMovies();
        } else {

            if (tryAgain()) {
                //Call to async task to get the data
                getMovies().fetchAllMovies(type);

            } else {
                //Set the text of textview visible if the connection is not there
                tv_no_Connection.setVisibility(View.VISIBLE);

            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

    }

    // Create a method to check if internet is present or not
    public boolean tryAgain() {
        if (!NetworkUtils.isNetworkConnected(this)) {
            return false;
        } else {

            return true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("sort_type", sort_id);
        outState.putString("type", type);
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        switch (sort_id) {
            case R.id.top_rated:
                try {

                    results.clear();
                    type = "top_rated";

                    getMovies().fetchAllMovies(type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.popular:
                try {
                    type = "popular";
                    results.clear();
                    getMovies().fetchAllMovies(type);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            case R.id.fav:
                type = "fav";
                tv_no_Connection.setVisibility(View.GONE);

                results.clear();
                getSavedMovies();

        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item1) {
        //Handle menu options click

        switch (item1.getItemId()) {
            case R.id.top_rated:
                try {
                    sort_id = R.id.top_rated;
                    results.clear();
                    no_favourite.setVisibility(View.GONE);
                    type = "top_rated";

                    getMovies().fetchAllMovies(type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.popular:
                try {
                    type = "popular";
                    sort_id = R.id.popular;
                    results.clear();
                    tv_no_Connection.setVisibility(View.GONE);

                    no_favourite.setVisibility(View.GONE);
                    getMovies().fetchAllMovies(type);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            case R.id.fav:
                results.clear();
                type = "fav";
                sort_id = R.id.fav;
                tv_no_Connection.setVisibility(View.GONE);
                no_favourite.setVisibility(View.GONE);
                getSavedMovies();

            default:
                return super.onOptionsItemSelected(item1);
        }
    }

    public void getSavedMovies() {

        String query = "SELECT " + MoviesContract.MoviesData.TABLE_NAME + "." + MoviesContract.MoviesData.COLUMN_MOVIE_NAME + ","
                + MoviesContract.MoviesData.TABLE_NAME + "." + MoviesContract.MoviesData.COLUMN_MOVIE_ID + "," +
                MoviesContract.MoviesData.TABLE_NAME_2 + "." + MoviesContract.MoviesData.COLUMN_TRAILER_IMAGE_PATH + ","
                + MoviesContract.MoviesData.TABLE_NAME_2 + "." + MoviesContract.MoviesData.COLUMN_TRAILER_ID + " FROM "
                + MoviesContract.MoviesData.TABLE_NAME + " LEFT JOIN " + MoviesContract.MoviesData.TABLE_NAME_2 + " ON " +
                MoviesContract.MoviesData.TABLE_NAME + "." + MoviesContract.MoviesData.COLUMN_MOVIE_ID + "=" + MoviesContract.MoviesData.TABLE_NAME_2
                + "." + MoviesContract.MoviesData.COLUMN_TRAILER_ID +
                " group by " + MoviesContract.MoviesData.TABLE_NAME + "."
                + MoviesContract.MoviesData.COLUMN_MOVIE_ID;
        Cursor c = mDb.rawQuery(query, null);
        if (!(c.moveToFirst()) || c.getCount() == 0) {
            //cursor is empty
            results.clear();
            no_favourite.setVisibility(View.VISIBLE);


        } else {

            try {

                if (c.moveToFirst()) {
                    do {
                        results.add(new MoviePoster(c.getString(c.getColumnIndex(MoviesContract.MoviesData.COLUMN_TRAILER_IMAGE_PATH)),
                                c.getString(c.getColumnIndex(MoviesContract.MoviesData.COLUMN_MOVIE_ID))));
                    } while (c.moveToNext());

                    adapter = new MovieRecyclerAdapter(MainActivity.this, results);
                    rv.setAdapter(adapter);
                    tv_no_Connection.setVisibility(View.GONE);
                }
            } finally {
                c.close();
            }

        }
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        switch (sort_id) {
            case R.id.top_rated:
                try {

                    results.clear();
                    type = "top_rated";

                    getMovies().fetchAllMovies(type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.popular:
                try {
                    type = "popular";
                    results.clear();
                    getMovies().fetchAllMovies(type);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.fav:
                type = "fav";
                results.clear();
                getSavedMovies();
                break;

        }
        //Refresh your stuff here
    }

}
