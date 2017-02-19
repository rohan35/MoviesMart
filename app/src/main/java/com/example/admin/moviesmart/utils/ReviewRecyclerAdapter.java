package com.example.admin.moviesmart.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.moviesmart.R;
import com.example.admin.moviesmart.model.MovieReview;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 1/8/2017.
 */

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewHolder> {
    Context context;
    ArrayList<MovieReview> mReview = new ArrayList<>();

    public ReviewRecyclerAdapter(Context c, ArrayList<MovieReview> review) {
        this.context = c;
        this.mReview = review;
    }

    @Override
    public ReviewRecyclerAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reviews_layout, parent, false);
        ReviewHolder reviewHolder = new ReviewHolder(v);
        return reviewHolder;

    }

    @Override
    public void onBindViewHolder(ReviewRecyclerAdapter.ReviewHolder holder, int position) {
        holder.reviews.setText(mReview.get(position).getmReview());
        holder.author.setText(mReview.get(position).getmAuthor());

    }

    @Override
    public int getItemCount() {
        return mReview != null ? mReview.size() : 0;
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.review_text)
        TextView reviews;
        @BindView(R.id.author)
        TextView author;

        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
