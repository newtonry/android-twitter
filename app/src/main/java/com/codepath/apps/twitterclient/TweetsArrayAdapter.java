package com.codepath.apps.twitterclient;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by rnewton on 8/15/16.
 */
public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.ViewHolder> {

    private List<Tweet> tweets;
    private Context context;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        this.tweets = tweets;
        this.context = context;
    }


    private Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View tweetView = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        final ViewHolder viewHolder = new ViewHolder(tweetView);

        tweetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimelineActivity act = (TimelineActivity) getContext();
                act.launchDetailedActivity(viewHolder.tweetId);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Tweet tweet = tweets.get(position);
        viewHolder.tvName.setText(tweet.getUser().getName());
        viewHolder.tvUserName.setText(tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvTime.setText(tweet.getTimeDifference());
        viewHolder.tweetId = tweet.getUid();

        viewHolder.ivProfileImage.setImageResource(0);
        viewHolder.ivMediaImage.setImageResource(0);
        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .transform(new RoundedCornersTransformation(3, 3))
                .into(viewHolder.ivProfileImage);

        if (!tweet.getMediaUrl().isEmpty()) {
            viewHolder.ivMediaImage.setVisibility(View.VISIBLE);
            Picasso.with(getContext())
                    .load(tweet.getMediaUrl())
                    .transform(new RoundedCornersTransformation(15, 15))
                    .into(viewHolder.ivMediaImage);
        } else {
            viewHolder.ivMediaImage.setVisibility(View.GONE);
        }

        viewHolder.setupFonts(getContext());
    }

    @Override
    public int getItemCount() {
        Log.v("s", Integer.toString(tweets.size()));
        return tweets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        Long tweetId;
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.tvBody) TextView tvBody;
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.ivMediaImage) ImageView ivMediaImage;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setupFonts(Context context) {
            AssetManager assets = context.getAssets();
            tvName.setTypeface(Typeface.createFromAsset(assets, "Helvetica Neue LT Pro 75 Bold.ttf"));
            tvBody.setTypeface(Typeface.createFromAsset(assets, "Helvetica Neue LT Pro 55 Roman.ttf"));
            tvUserName.setTypeface(Typeface.createFromAsset(assets, "Helvetica Neue LT Pro 55 Roman.ttf"));
            tvTime.setTypeface(Typeface.createFromAsset(assets, "Helvetica Neue LT Pro 55 Roman.ttf"));
        }

    }



}
