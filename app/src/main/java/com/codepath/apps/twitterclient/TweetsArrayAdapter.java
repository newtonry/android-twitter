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
import android.widget.Toast;

import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
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
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View tweetView = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        final ViewHolder viewHolder = new ViewHolder(tweetView);

        tweetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimelineActivity act = (TimelineActivity) getContext();
                act.launchDetailedActivity(viewHolder.tweetId);
            }
        });

        viewHolder.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimelineActivity act = (TimelineActivity) getContext();
                act.launchComposeView(view, viewHolder.userName);
            }
        });

        viewHolder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterClient client = new TwitterClient(parent.getContext());
                client.favoriteTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.v("debug", "favorited");
                        Toast.makeText(context, "Favorited!", Toast.LENGTH_SHORT).show();
                        viewHolder.btnFavorite.setColorFilter(parent.getContext().getResources().getColor(android.R.color.holo_red_dark));
                    }
                }, viewHolder.tweetId);
            }
        });

        viewHolder.btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterClient client = new TwitterClient(parent.getContext());
                client.retweetTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.v("debug", "retweeted");
                        Toast.makeText(context, "Retweeted!", Toast.LENGTH_SHORT).show();
                        viewHolder.btnRetweet.setColorFilter(parent.getContext().getResources().getColor(R.color.twitterRetweetGreen));
                    }
                }, viewHolder.tweetId);
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
        viewHolder.userName = tweet.getUser().getScreenName();

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

        if (tweet.isFavorited()) {
            viewHolder.btnFavorite.setColorFilter(context.getResources().getColor(android.R.color.holo_red_dark));
        }
        if (tweet.isRetweeted()) {
            viewHolder.btnRetweet.setColorFilter(context.getResources().getColor(R.color.twitterRetweetGreen));
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
        String userName;
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.tvBody) TextView tvBody;
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.ivMediaImage) ImageView ivMediaImage;
        @BindView(R.id.btnReply) ImageView btnReply;
        @BindView(R.id.btnRetweet) ImageView btnRetweet;
        @BindView(R.id.btnFavorite) ImageView btnFavorite;


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
