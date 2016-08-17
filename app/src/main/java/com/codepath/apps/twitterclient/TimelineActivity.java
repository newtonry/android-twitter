package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetsArrayAdapter adapter;
    private ArrayList<Tweet> tweets;

    @BindView(R.id.lvTweets) RecyclerView lvTweets;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        tweets = new ArrayList<>();
        adapter = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lvTweets.setLayoutManager(linearLayoutManager);

        lvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                client.getHomeTimeline(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                        tweets.addAll(Tweet.fromJSONArray(json));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("test", errorResponse.toString());
                    }
                }, getLastId());
            }
        });


        setupRefresher();

        client = TwitterApplication.getRestClient();
        populateTimeline();


//        launchComposeView();
    }


    private void setupRefresher() {


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Long mostRecentId = tweets.get(0).getUid();
                client.getRecentPosts(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                        tweets.addAll(Tweet.fromJSONArray(json));
                        adapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("test", errorResponse.toString());
                    }
                }, mostRecentId);
            }
        });
        swipeContainer.setColorSchemeResources(R.color.twitterPrimaryBlue);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    private Long getLastId() {
        return tweets.get(tweets.size() - 1).getUid();
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                tweets.addAll(Tweet.fromJSONArray(json));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("test", errorResponse.toString());

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.no_change,R.anim.slide_down);

        if (resultCode == 200) {
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            tweets.add(0, tweet);
            adapter.notifyDataSetChanged();
        }
    }

    public void launchComposeView(View view) {
        Log.v("log", "launching new tweet");
        Intent i = new Intent(TimelineActivity.this, NewTweetActivity.class);
        startActivityForResult(i, 200);
        overridePendingTransition(R.anim.slide_up, R.anim.no_change);
    }
}
