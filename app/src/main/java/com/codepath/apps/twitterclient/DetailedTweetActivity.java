package com.codepath.apps.twitterclient;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterclient.models.Tweet;

import org.parceler.Parcels;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class DetailedTweetActivity extends BaseTwitterActivity {

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
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        render();
    }

    private void render() {
        tvName.setText(tweet.getUser().getName());
        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvFavoriteCount.setText(Integer.toString(tweet.getFavoriteCount()));
        tvRetweetCount.setText(Integer.toString(tweet.getRetweetCount()));
        tvDatePosted.setText(CustomUtils.getDateTimeString(tweet.getCreatedAt()));

        Glide.with(this).load(tweet.getUser().getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(this, 3, 0))
                .into(ivProfileImage);

        if (!tweet.getMediaUrl().isEmpty()) {
            Glide.with(this).load(tweet.getMediaUrl())
                    .bitmapTransform(new RoundedCornersTransformation(this, 20, 0))
                    .into(ivMediaImage);
        }

        if (tweet.isFavorited()) {
            btnFavorite.setColorFilter(getResources().getColor(android.R.color.holo_red_dark));
        }
        if (tweet.isRetweeted()) {
            btnRetweet.setColorFilter(getResources().getColor(R.color.twitterRetweetGreen));
        }

        stylizeBody();
    }

    public void stylizeBody() {
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), getResources().getColor(R.color.twitterPrimaryBlue),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                launchComposeView(text);
                            }
                        }).into(tvBody);

        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\#(\\w+)"), getResources().getColor(R.color.twitterPrimaryBlue),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                launchComposeView(text);
                            }
                        }).into(tvBody);
    }

    public void closeDetailedTweetActivity(View view) {
        finish();
    }
}
