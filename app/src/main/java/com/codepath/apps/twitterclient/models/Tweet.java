package com.codepath.apps.twitterclient.models;

import com.codepath.apps.twitterclient.CustomUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by rnewton on 8/15/16.
 */



//@Table(name = "Tweets")
@Parcel
public class Tweet {
//    @Column(name = "body") String body;
//    @Column(name = "uid", unique = true) Long uid;
//    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE) User user;
//    @Column(name = "createdAt") String createdAt;
    String body;
    Long uid;
    User user;
    String createdAt;


    public String getCreatedAt() {
        return createdAt;
    }

    public String getBody() {
        return body;
    }

    public Long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getTimeDifference() {
        return CustomUtils.getRelativeTimeAgo(createdAt);
    }

    public Tweet() {

    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");


            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                tweets.add(Tweet.fromJSON(tweetJson));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }
}
