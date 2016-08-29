package com.codepath.apps.twitterclient.models;

import android.util.Log;

import com.codepath.apps.twitterclient.utils.CustomUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by rnewton on 8/15/16.
 */


@Parcel
public class Tweet {
    String body;
    String retweetedBody;
    Long uid;
    User user;
    User retweetedUser;
    String createdAt;
    String mediaUrl;
    int retweetCount;
    int favoriteCount;
    Boolean favorited;
    Boolean retweeted;
    public Boolean wasRetweetedByUser = false;


    public String getCreatedAt() {
        return createdAt;
    }
    public String getBody() {
        return body;
    }
    public String getRetweetedBody() {
        return retweetedBody;
    }
    public String getMediaUrl() {
        return mediaUrl;
    }
    public Long getUid() {
        return uid;
    }
    public User getUser() {
        return user;
    }
    public User getRetweetedUser() {
        return retweetedUser;
    }
    public String getTimeDifference() {
        return CustomUtils.getRelativeTimeAgo(createdAt);
    }
    public int getRetweetCount() { return retweetCount; }
    public int getFavoriteCount() { return favoriteCount; }
    public Boolean isFavorited() {
        return favorited;
    }
    public Boolean isRetweeted() {
        return retweeted;
    }
    public Boolean getWasRetweetedByUser() {
        return wasRetweetedByUser;
    }

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    public void setRetweeted(Boolean retweeted) {
        this.retweeted = retweeted;
    }

    public Tweet() {

    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            tweet.favoriteCount = jsonObject.getInt("favorite_count");
            tweet.favorited = jsonObject.getBoolean("favorited");
            tweet.retweeted = jsonObject.getBoolean("retweeted");

            if (jsonObject.has("retweeted_status")) {
                tweet.wasRetweetedByUser = true;
                JSONObject retweetedStatus = jsonObject.getJSONObject("retweeted_status");
                tweet.retweetedBody = retweetedStatus.getString("text");
                JSONObject retweetedUserJson = retweetedStatus.getJSONObject("user");
                tweet.retweetedUser = User.fromJSON(retweetedUserJson);
                Log.v("reee", retweetedUserJson.toString());

            }

            tweet.mediaUrl = "";
            if (jsonObject.has("extended_entities")) {
                JSONObject extendedEntities = jsonObject.getJSONObject("extended_entities");
                JSONArray media = extendedEntities.getJSONArray("media");
                if (media.length() > 0) {
                    JSONObject firstMedia = media.getJSONObject(0);
                    tweet.mediaUrl = firstMedia.getString("media_url");
                }
            }

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
