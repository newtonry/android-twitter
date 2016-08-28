package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.models.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by rnewton on 8/27/16.
 */
public class ProfileFragment extends Fragment {

    View mainView;
    User user;

    @BindView(R.id.ivUserBackground) ImageView ivUserBackground;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvUserScreenName) TextView tvUserScreenName;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.tvLink) TextView tvLink;


    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setUser(user);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        TwitterApplication application = (TwitterApplication) getActivity().getApplication();
        user = application.user;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.fragment_profile, container, false);
        }
        return mainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        setupFields();
    }

    private void setupFields() {
        Log.v("sadfsd", user.getProfileBackgroundImageUrl());


        Picasso.with(getContext())
                .load(user.getProfileBackgroundImageUrl())
                .into(ivUserBackground);

        Glide.with(getContext())
                .load(user.getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(getContext(), 3, 0))
                .into(ivProfileImage);

        tvUserName.setText(user.getName());
        tvUserScreenName.setText("@" + user.getScreenName());
        tvDescription.setText(user.getDescription());
    }

    public void setUser(User user) {

        this.user = user;
    }
}
