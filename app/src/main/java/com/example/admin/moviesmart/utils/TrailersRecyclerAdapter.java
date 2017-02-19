package com.example.admin.moviesmart.utils;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.moviesmart.R;
import com.example.admin.moviesmart.model.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.admin.moviesmart.R.mipmap.ic_launcher;

/**
 * Created by admin on 1/7/2017.
 */

public class TrailersRecyclerAdapter extends RecyclerView.Adapter<TrailersRecyclerAdapter.ItemHolder> {
    ArrayList<MovieTrailer> trailers = new ArrayList<MovieTrailer>();
    Context context;

    public TrailersRecyclerAdapter(Context c, ArrayList<MovieTrailer> trailer) {
        this.context = c;
        this.trailers = trailer;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.trailers, parent, false);
        final ItemHolder viewHolder = new ItemHolder(v);

        viewHolder.play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MovieTrailer curr_position = trailers.get(viewHolder.getAdapterPosition());
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + curr_position.getmId()));

                context.startActivity(i);
            }
        });
        viewHolder.share.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                MovieTrailer curr_position = trailers.get(viewHolder.getAdapterPosition());
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + curr_position.getmId());
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent,
                        "Share link using: "));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        if(position==0)
        {

            holder.share.setVisibility(View.VISIBLE);
        }

        holder.trailerName.setText(trailers.get(position).getMtrailerName());
        if (trailers.get(position).getImagePath().contains("ImageFolder")) {
            Picasso.with(context).load(new File(trailers.get(position).getImagePath())).into(holder.iv);

        } else {


            Picasso.with(context).load(trailers.get(position).getImagePath()).into(holder.iv);

        }

    }


    @Override
    public int getItemCount() {
        return trailers != null ? trailers.size() : 0;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.trailer_name)
        TextView trailerName;
        @BindView(R.id.img_view)
        ImageView iv;
        @BindView(R.id.play)
        ImageButton play;
        @BindView(R.id.share)
        ImageButton share;

        public ItemHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
