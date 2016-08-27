package com.codepath.apps.twitterclient;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "6ygYd6B5zLbO0SrVhtGBAdyfB";       // Change this
	public static final String REST_CONSUMER_SECRET = "MkfjAASZWMelER4UdkykmcSkWFa0wAR08RaHwYpTRkmm0BPyPl"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cptwitterclient"; // Change this (here and in manifest)

	static String TIMELINE_URL = "statuses/home_timeline.json";
	static String NEW_STATUS_URL = "statuses/update.json";
	static String TWEET_BY_ID = "statuses/show.json";
	static String FAVORITE_TWEET = "favorites/create.json";
	static String RETWEET_TWEET = "statuses/retweet/";
	static String ACCOUNT_SETTINGS = "account/settings.json";
	static String VERIFY_CREDENTIALS = "account/verify_credentials.json";
	static String USER_LOOKUP = "users/lookup.json";
	static String MENTIONS_TIMELINE = "statuses/mentions_timeline.json";
	static String USER_TIMELINE = "statuses/user_timeline.json";




	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler callback, Long maxId) {
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
		params.put("max_id", maxId);
		getClient().get(REST_URL + TIMELINE_URL, params, callback);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler callback) {
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
		getClient().get(REST_URL + TIMELINE_URL, params, callback);
	}

	public void getRecentPosts(AsyncHttpResponseHandler callback, Long minId) {
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", minId);
		getClient().get(REST_URL + TIMELINE_URL, params, callback);
	}

	public void postNewStatus(AsyncHttpResponseHandler callback, String status) {
		RequestParams params = new RequestParams();
		params.put("status", status);
		getClient().post(REST_URL + NEW_STATUS_URL, params, callback);

	}

	public void getTweetById(AsyncHttpResponseHandler callback, Long id) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		getClient().get(REST_URL + TWEET_BY_ID, params, callback);
	}

	public void favoriteTweet(AsyncHttpResponseHandler callback, Long id) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		getClient().post(REST_URL + FAVORITE_TWEET, params, callback);
	}

	public void retweetTweet(AsyncHttpResponseHandler callback, Long id) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		getClient().post(REST_URL + RETWEET_TWEET + id + ".json", params, callback);
	}

	public void getUserTimeline(AsyncHttpResponseHandler callback, String screenName) {
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		params.put("count", 25);
		getClient().get(REST_URL + USER_TIMELINE, params, callback);
	}

	public void getUserInfo(AsyncHttpResponseHandler callback) {
		getClient().get(REST_URL + VERIFY_CREDENTIALS, null, callback);
	}


	public void getMentionsTimeline(JsonHttpResponseHandler callback) {
		RequestParams params = new RequestParams();
		params.put("count", 25);
		getClient().get(REST_URL + MENTIONS_TIMELINE, params, callback);
	}



	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}