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

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by rnewton on 8/15/16.
 */
public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.ViewHolder> {

//    private OnProfileSelectedListener listener;
//    public interface OnProfileSelectedListener {
//        public void onProfileSelected(User user);
//    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

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
                ControllerActivity act = (ControllerActivity) getContext();
                act.launchDetailedActivity(viewHolder.tweet);
            }
        });

        viewHolder.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControllerActivity act = (ControllerActivity) getContext();
                act.launchComposeView(view, viewHolder.tweet.getUser().getScreenName());
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
                }, viewHolder.tweet.getUid());
            }
        });

        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControllerActivity act = (ControllerActivity) getContext();
                act.launchProfile(viewHolder.tweet.getUser());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Tweet tweet = tweets.get(position);
        User user = tweet.getWasRetweetedByUser() ? tweet.getRetweetedUser() : tweet.getUser();
        String body = tweet.getWasRetweetedByUser() ? tweet.getRetweetedBody() : tweet.getBody();

        viewHolder.tvName.setText(user.getName());
        viewHolder.tvUserName.setText(user.getScreenName());
        viewHolder.tvBody.setText(body);
        viewHolder.tvTime.setText(tweet.getTimeDifference());
        viewHolder.tweet = tweet;

        if (tweet.getWasRetweetedByUser()) {
            viewHolder.tvNameRetweeted.setVisibility(View.VISIBLE);
            viewHolder.ivRetweetedStatus.setVisibility(View.VISIBLE);
            viewHolder.tvNameRetweeted.setText(tweet.getUser().getName() + " Retweeted");
        } else {
            viewHolder.tvNameRetweeted.setVisibility(View.GONE);
            viewHolder.ivRetweetedStatus.setVisibility(View.GONE);

        }

        viewHolder.ivProfileImage.setImageResource(0);
        viewHolder.ivMediaImage.setImageResource(0);
        Glide.with(getContext())
                .load(user.getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(getContext(), 3, 0))
                .into(viewHolder.ivProfileImage);

        if (!tweet.getMediaUrl().isEmpty()) {
            viewHolder.ivMediaImage.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(tweet.getMediaUrl())
                    .bitmapTransform(new RoundedCornersTransformation(getContext(), 8, 0))
                    .into(viewHolder.ivMediaImage);
        } else {
            viewHolder.ivMediaImage.setVisibility(View.GONE);
        }

        viewHolder.renderFavoritesAndRetweets();

        viewHolder.setupFonts(getContext());
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        Context context;
        Tweet tweet;
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUserName) TextView tvUserName;
        @BindView(R.id.tvBody) TextView tvBody;
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.ivMediaImage) ImageView ivMediaImage;
        @BindView(R.id.btnReply) ImageView btnReply;
        @BindView(R.id.btnRetweet) ImageView btnRetweet;
        @BindView(R.id.btnFavorite) ImageView btnFavorite;
        @BindView(R.id.tvNameRetweeted) TextView tvNameRetweeted;
        @BindView(R.id.ivRetweetedStatus) ImageView ivRetweetedStatus;
        @BindView(R.id.tvRetweetCount) TextView tvRetweetCount;
        @BindView(R.id.tvFavoriteCount) TextView tvFavoriteCount;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @OnClick(R.id.btnFavorite)
        public void onFavorite(View view) {
            TwitterClient client = client = TwitterApplication.getRestClient();
            if (tweet.isFavorited()) {
                client.unfavoriteTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.v("debug", "favorited");
                        Toast.makeText(context, "Unfavorited!", Toast.LENGTH_SHORT).show();
                        tweet = Tweet.fromJSON(response);
                        btnFavorite.setColorFilter(context.getResources().getColor(R.color.twitterLightGray));
                        renderFavoritesAndRetweets();
                    }
                }, tweet.getUid());
            } else {
                client.favoriteTweet(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.v("debug", "favorited");
                        tweet = Tweet.fromJSON(response);
                        Toast.makeText(context, "Favorited!", Toast.LENGTH_SHORT).show();
                        btnFavorite.setColorFilter(context.getResources().getColor(android.R.color.holo_red_dark));
                        renderFavoritesAndRetweets();
                    }
                }, tweet.getUid());
            }
        }

        // TODO There must be a more legit way to do this by passing it up to the adapter?
        public void renderFavoritesAndRetweets() {
            if (tweet.isFavorited()) {
                btnFavorite.setColorFilter(context.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                btnFavorite.setColorFilter(context.getResources().getColor(R.color.twitterLightGray));
            }
            if (tweet.isRetweeted()) {
                btnRetweet.setColorFilter(context.getResources().getColor(R.color.twitterRetweetGreen));
            } else {
                btnRetweet.setColorFilter(context.getResources().getColor(R.color.twitterLightGray));
            }
            tvFavoriteCount.setText(Integer.toString(tweet.getFavoriteCount()));
            tvRetweetCount.setText(Integer.toString(tweet.getRetweetCount()));
        }


        private void setupFonts(Context context) {
            AssetManager assets = context.getAssets();
            tvName.setTypeface(Typeface.createFromAsset(assets, "Helvetica Neue LT Pro 75 Bold.ttf"));
            tvBody.setTypeface(Typeface.createFromAsset(assets, "Helvetica Neue LT Pro 55 Roman.ttf"));
            tvUserName.setTypeface(Typeface.createFromAsset(assets, "Helvetica Neue LT Pro 55 Roman.ttf"));
            tvTime.setTypeface(Typeface.createFromAsset(assets, "Helvetica Neue LT Pro 55 Roman.ttf"));
            tvNameRetweeted.setTypeface(Typeface.createFromAsset(assets, "Helvetica Neue LT Pro 55 Roman.ttf"));
        }
    }
}
