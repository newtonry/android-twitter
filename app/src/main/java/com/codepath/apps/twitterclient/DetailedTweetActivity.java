package com.codepath.apps.twitterclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;

public class DetailedTweetActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_tweet);
        ButterKnife.bind(this);
    }


    public void closeDetailedTweetActivity(View view) {
        finish();
    }
}
