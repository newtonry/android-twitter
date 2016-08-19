package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DetailedTweetActivity extends AppCompatActivity {

    Tweet tweet;

    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.ivMediaImage) ImageView ivMediaImage;
    @BindView(R.id.tvRetweetCount) TextView tvRetweetCount;
    @BindView(R.id.tvFavoriteCount) TextView tvFavoriteCount;
    @BindView(R.id.tvDatePosted) TextView tvDatePosted;
    @BindView(R.id.btnRetweet) ImageButton btnRetweet;
    @BindView(R.id.btnFavorite) ImageButton btnFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_tweet);
        ButterKnife.bind(this);

        Intent i = getIntent();
        Long tweetId = i.getLongExtra("tweetId", 0);

        TwitterClient client = new TwitterClient(this);

        client.getTweetById(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                tweet = Tweet.fromJSON(response);
                render();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("test", errorResponse.toString());
            }
        }, tweetId);
    }

    private void render() {
        tvName.setText(tweet.getUser().getName());
        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvFavoriteCount.setText(Integer.toString(tweet.getFavoriteCount()));
        tvRetweetCount.setText(Integer.toString(tweet.getRetweetCount()));
        tvDatePosted.setText(CustomUtils.getDateTimeString(tweet.getCreatedAt()));

        Picasso.with(this).load(tweet.getUser().getProfileImageUrl())
                .transform(new RoundedCornersTransformation(3, 3))
                .into(ivProfileImage);

        if (!tweet.getMediaUrl().isEmpty()) {
            Picasso.with(this).load(tweet.getMediaUrl())
                    .transform(new RoundedCornersTransformation(20, 20))
                    .into(ivMediaImage);
        }

        if (tweet.isFavorited()) {
            btnFavorite.setColorFilter(getResources().getColor(android.R.color.holo_red_dark));
        }
        if (tweet.isRetweeted()) {
            btnRetweet.setColorFilter(getResources().getColor(R.color.twitterRetweetGreen));
        }


    }

    public void closeDetailedTweetActivity(View view) {
        finish();
    }
}
