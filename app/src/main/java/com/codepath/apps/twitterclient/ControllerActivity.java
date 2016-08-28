package com.codepath.apps.twitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterclient.activities.ProfileActivity;
import com.codepath.apps.twitterclient.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterclient.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ControllerActivity extends AppCompatActivity {

    private HomeTimelineFragment fragmentTweetsList;

    @BindView(R.id.btnNewTweet) ImageButton btnNewTweet;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            overridePendingTransition(R.anim.no_change,R.anim.slide_down);
        } else if (requestCode == 100) {
            overridePendingTransition(R.anim.no_change,R.anim.slide_right);
        }

        if (resultCode == 200) {
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            fragmentTweetsList.addTweet(tweet);
        }
    }

    @OnClick(R.id.btnNewTweet)
    public void launchComposeView(View view) {
        Log.v("log", "launching new tweet");
        Intent i = new Intent(ControllerActivity.this, NewTweetActivity.class);
        i.putExtra("prefill", "");
        startActivityForResult(i, 200);
        overridePendingTransition(R.anim.slide_up, R.anim.no_change);
    }

    public void launchComposeView(View view, String prefill) {
        Log.v("log", "launching new tweet");
        Intent i = new Intent(ControllerActivity.this, NewTweetActivity.class);
        i.putExtra("prefill", prefill);
        startActivityForResult(i, 200);
        overridePendingTransition(R.anim.slide_up, R.anim.no_change);
    }

    public void launchDetailedActivity(Tweet tweet) {
        Log.v("log", "launching detailed");
        Intent i = new Intent(ControllerActivity.this, DetailedTweetActivity.class);
        i.putExtra("tweet", Parcels.wrap(tweet));
        startActivityForResult(i, 100);
        overridePendingTransition(R.anim.slide_left, R.anim.no_change);
    }


    public void launchProfile(User user) {

        Intent i = new Intent(this, ProfileActivity.class);
//        i.putExtra("screenName", user.getScreenName());
        i.putExtra("user", Parcels.wrap(user));
        startActivity(i);




//        viewPager.setCurrentItem(2);
//        ProfileFragment fragment = ProfileFragment.newInstance(user);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.fragmentTimeline, fragment);
//        ft.commit();
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
//            } else if (position == 2) {
//                return new ProfileFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

}
