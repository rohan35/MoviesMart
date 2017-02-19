package com.example.admin.moviesmart.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.admin.moviesmart.R;
import com.example.admin.moviesmart.data.MoviesContract;
import com.example.admin.moviesmart.data.MoviesDbHelper;
import com.example.admin.moviesmart.model.MovieReview;
import com.example.admin.moviesmart.utils.ReviewRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 1/8/2017.
 */

public class Reviews extends AppCompatActivity {
    @BindView(R.id.rv_reviews)
    RecyclerView rv_review;
    @BindView(R.id.no_reviews)
    TextView tv;
    SQLiteDatabase mDb;
    ReviewRecyclerAdapter adapter;
    ArrayList<MovieReview> movieReviews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews);
        ButterKnife.bind(this);
        MoviesDbHelper movies_helper = new MoviesDbHelper(this);
        mDb = movies_helper.getWritableDatabase();

        Bundle b = getIntent().getExtras();


        if (b.getInt("CHECK") == 0) {
            //Movie exist
            String id = b.getString("ID");

            String query = "select * from " + MoviesContract.MoviesData.TABLE_NAME_3 + " where " + MoviesContract.MoviesData.COLUMN_Review_ID
                    + "='" + id + "\'";
            Cursor c = mDb.rawQuery(query, null);
            if (!(c.moveToFirst()) && c.getCount() == 0) {
                tv.setVisibility(View.VISIBLE);
            } else {
                if (c.moveToFirst()) {
                    do {
                        movieReviews.add(new MovieReview(c.getString(c.getColumnIndex(MoviesContract.MoviesData.COLUMN_REVIEW_DETAIL))
                                , c.getString(c.getColumnIndex(MoviesContract.MoviesData.COLUMN_REVIEW_NAME))));

                    } while (c.moveToNext());
                }
            }
        } else if (b.getInt("CHECK") == 1) {

            String Array = b.getString("Array");
            try {
                JSONArray array = new JSONArray(Array);
                if (array.length() == 0) {
                    tv.setVisibility(View.VISIBLE);
                } else {
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject j = (JSONObject) array.get(i);
                        movieReviews.add(new MovieReview(j.getString("content"), j.getString("author")));
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter = new ReviewRecyclerAdapter(getApplication(), movieReviews);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_review.setHasFixedSize(true);
        rv_review.setLayoutManager(horizontalLayoutManager);
        rv_review.setAdapter(adapter);


    }
}
