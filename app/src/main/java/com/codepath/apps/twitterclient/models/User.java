package com.codepath.apps.twitterclient.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by rnewton on 8/15/16.
 */


@Parcel
public class User {

    String name;
    long uid;
    String screenName;
    String profileImageUrl;
    String profileBackgroundImageUrl;
    String description;
    int followersCount;
    int followingCount;

    public String getDescription() {
        return description;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return "@" + screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public User() {

    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.description = jsonObject.getString("description");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
//            user.profileBackgroundImageUrl = "https://pbs.twimg.com/profile_banners/14342094/1369149678/600x200";
            String imgString = jsonObject.getString("profile_banner_url");



            user.profileBackgroundImageUrl = imgString + "/600x200";
            user.followersCount = jsonObject.getInt("followers_count");
            user.followingCount = jsonObject.getInt("following");

            // TODO add link


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
