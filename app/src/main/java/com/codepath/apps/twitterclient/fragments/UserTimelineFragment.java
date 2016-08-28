package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rnewton on 8/27/16.
 */
public class UserTimelineFragment extends TweetsListFragment {

//    private TwitterClient client;
//
//    public static UserTimelineFragment newInstance(User user) {
//        UserTimelineFragment fragmentUserTimeline = new UserTimelineFragment();
//        Bundle args = new Bundle();
//        args.putString("screenName", user.getScreenName());
//        fragmentUserTimeline.setArguments(args);
//        return fragmentUserTimeline;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        client = TwitterApplication.getRestClient();
////        populateTimeline();
//    }
//
//
//    private void populateTimeline() {
////        TwitterApplication application = (TwitterApplication) getActivity().getApplication();
////        user = application.user;
//        String screenName = getArguments().getString("screenName");
//
//
//
//        client.getUserTimeline(new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
//                addAll(Tweet.fromJSONArray(json));
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("test", errorResponse.toString());
//            }
//        }, screenName);
//    }


    private TwitterClient client;



    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }






    private void populateTimeline() {
        String screenName = getArguments().getString("screenName");

        client.getUserTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Log.v("fetching", "dasfasdfdsfdss");
                addAll(Tweet.fromJSONArray(json));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("test", errorResponse.toString());
            }
        }, screenName);
    }





}
