package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class NewTweetActivity extends AppCompatActivity {

    @BindView(R.id.btnPostTweet) Button btnPostTweet;
    @BindView(R.id.etMessage) EditText etMessage;
    @BindView(R.id.tvCharactersLeft) TextView tvCharactersLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tweet);
        ButterKnife.bind(this);
        setupListeners();
    }

    private void setupListeners() {
        btnPostTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTweet();

            }
        });

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int charactersLeft = 140 - charSequence.length();
                tvCharactersLeft.setText(Integer.toString(charactersLeft));
                if (charactersLeft >= 0 && charSequence.length() > 0) {
                    enablePostButton();
                } else {
                    disablePostButton();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void enablePostButton() {
        btnPostTweet.setTextColor(getResources().getColor(R.color.twitterWhite));
        btnPostTweet.setBackgroundColor(getResources().getColor(R.color.twitterPrimaryBlue));
        btnPostTweet.setEnabled(true);
    }

    private void disablePostButton() {
        btnPostTweet.setTextColor(getResources().getColor(R.color.twitterLightGray));
        btnPostTweet.setBackgroundColor(getResources().getColor(R.color.twitterWhite));
        btnPostTweet.setEnabled(false);
    }


    private void submitTweet() {
        String status = etMessage.getText().toString();
        TwitterClient twitterClient = new TwitterClient(this);
        twitterClient.postNewStatus(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet tweet = Tweet.fromJSON(response);
                Intent data = new Intent();
                data.putExtra("tweet", Parcels.wrap(tweet));
                setResult(200, data);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("error", errorResponse.toString());
            }
        }, status);
    }
}
