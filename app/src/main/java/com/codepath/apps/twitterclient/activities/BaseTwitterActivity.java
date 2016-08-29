package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.apps.twitterclient.DetailedTweetActivity;
import com.codepath.apps.twitterclient.NewTweetActivity;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;

import org.parceler.Parcels;

/**
 * Created by rnewton on 8/28/16.
 * There are a few common actions, so I want to add them here.
 */

public class BaseTwitterActivity extends AppCompatActivity implements TweetsArrayAdapter.TweetActionListener {
    @Override
    public void launchComposeView(String prefill) {
        Log.v("log", "launching new tweet");
        Intent i = new Intent(this, NewTweetActivity.class);
        i.putExtra("prefill", prefill);
        startActivityForResult(i, 200);
        overridePendingTransition(R.anim.slide_up, R.anim.no_change);
    }

    @Override
    public void launchProfile(User user) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("user", Parcels.wrap(user));
        startActivity(i);
    }

    @Override
    public void launchDetailedActivity(Tweet tweet) {
        Log.v("log", "launching detailed");
        Intent i = new Intent(this, DetailedTweetActivity.class);
        i.putExtra("tweet", Parcels.wrap(tweet));
        startActivityForResult(i, 100);
        overridePendingTransition(R.anim.slide_left, R.anim.no_change);
    }
}
