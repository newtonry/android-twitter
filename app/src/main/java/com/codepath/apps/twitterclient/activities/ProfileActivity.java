package com.codepath.apps.twitterclient.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterclient.BaseTwitterActivity;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.twitterclient.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends BaseTwitterActivity {

    User user;
    @BindView(R.id.ivUserBackground) ImageView ivUserBackground;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvUserScreenName) TextView tvUserScreenName;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.tvLink) TextView tvLink;
    @BindView(R.id.tvFollowersCount) TextView tvFollowersCount;
    @BindView(R.id.tvFollowingCount) TextView tvFollowingCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setupFields();

        if (savedInstanceState == null) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(user.getScreenName());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }

    private void setupFields() {
        Picasso.with(this)
                .load(user.getProfileBackgroundImageUrl())
                .into(ivUserBackground);

        Glide.with(this)
                .load(user.getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(this, 3, 0))
                .into(ivProfileImage);

        tvUserName.setText(user.getName());
        tvUserScreenName.setText(user.getScreenName());
        tvDescription.setText(user.getDescription());
        tvFollowersCount.setText(Integer.toString(user.getFollowersCount()));
        tvFollowingCount.setText(Integer.toString(user.getFollowingCount()));
    }
}
