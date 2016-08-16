package com.codepath.apps.twitterclient;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by rnewton on 8/15/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.tvName) TextView tvName;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Tweet tweet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        ButterKnife.bind(this, convertView);

        tvName.setText(tweet.getUser().getName());
        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvTime.setText(tweet.getTimeDifference());


        ivProfileImage.setImageResource(0);
        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .transform(new RoundedCornersTransformation(3, 3))
                .into(ivProfileImage);

        return convertView;
    }

    private void setupFonts() {

        AssetManager assets = getContext().getAssets();

        tvName.setTypeface(Typeface.createFromAsset(assets, "Helvetica Neu Bold.ttf"));
        tvBody.setTypeface(Typeface.createFromAsset(assets, "HelveticaNeueLt.ttf"));
        tvUserName.setTypeface(Typeface.createFromAsset(assets, "HelveticaNeueLt.ttf"));
        tvTime.setTypeface(Typeface.createFromAsset(assets, "HelveticaNeueLt.ttf"));


    }

}
