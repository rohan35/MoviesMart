package com.example.admin.moviesmart.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.admin.moviesmart.activities.MovieDetails;
import com.squareup.picasso.Picasso;
import com.example.admin.moviesmart.R;
import com.example.admin.moviesmart.model.MoviePoster;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 12/30/2016.
 */

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.ItemViewHolder> {
    ArrayList<MoviePoster> mPoster = new ArrayList<MoviePoster>();
    private Context context;
    private LayoutInflater recipyInf;

    public MovieRecyclerAdapter(Context c, ArrayList<MoviePoster> poster) {
        this.context = c;
        recipyInf = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mPoster = poster;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = recipyInf.inflate(R.layout.rv_poster_layout, parent, false);
        final ItemViewHolder itemViewHolder = new ItemViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//get the current position of the song
                MoviePoster current_position = mPoster.get(itemViewHolder.getAdapterPosition());
                //create intent and pass id to MovieDetails
                Intent i = new Intent(context, MovieDetails.class);
                i.putExtra("ID", current_position.getId());
                context.startActivity(i);
            }
        });
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (mPoster.get(position).getImagePath().contains("ImageFolder")) {
            Picasso.with(context).load(new File(mPoster.get(position).getImagePath())).into(holder.poster_path);

        } else {
            Picasso.with(context).load(mPoster.get(position).getImagePath()).into(holder.poster_path);
        }
    }


    @Override
    public int getItemCount() {
        return mPoster != null ? mPoster.size() : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.poster)
        ImageView poster_path;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
