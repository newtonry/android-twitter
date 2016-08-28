package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclient.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TweetsArrayAdapter;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by rnewton on 8/24/16.
 */
public class TweetsListFragment extends Fragment {

    private TwitterClient client;

    private TweetsArrayAdapter adapter;
    private ArrayList<Tweet> tweets;


    @BindView(R.id.lvTweets) RecyclerView lvTweets;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        tweets = new ArrayList<>();
        adapter = new TweetsArrayAdapter(getActivity(), tweets);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        lvTweets.setAdapter(adapter);
        setupRefresher();
        setupScrollListener();
    }

    public void addAll(ArrayList<Tweet> moreTweets) {
        tweets.addAll(moreTweets);
        adapter.notifyDataSetChanged();
    }


    public void addTweet(Tweet tweet) {
        tweets.add(0, tweet);
        adapter.notifyDataSetChanged();
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
                        ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);
                        Collections.reverse(newTweets);
                        Collections.reverse(tweets);
                        tweets.addAll(newTweets);
                        Collections.reverse(tweets);
                        adapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("error", errorResponse.toString());
                    }
                }, mostRecentId);
            }
        });
        swipeContainer.setColorSchemeResources(R.color.twitterPrimaryBlue);
    }

    private void setupScrollListener() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        lvTweets.setLayoutManager(linearLayoutManager);

        lvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.v("dd","GFETTING MORE");
                fetchMore();
            }
        });
    }

    public void fetchMore() {}

    public void refreshTweets() {}

    public Long getLastId() {
        return tweets.get(tweets.size() - 1).getUid();
    }

}
