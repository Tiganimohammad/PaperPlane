package com.marktony.zhihudaily.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.favorites.FavoritesFragment;
import com.marktony.zhihudaily.favorites.FavoritesPresenter;
import com.marktony.zhihudaily.service.CacheService;
import com.marktony.zhihudaily.timeline.TimelineFragment;

/**
 * Created by lizhaotailang on 2017/5/20.
 */

public class MainActivity extends AppCompatActivity {

    private static final String KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID = "KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID";

    private TimelineFragment mTimelineFragment;
    private InfoFragment mInfoFragment;
    private FavoritesFragment mFavoritesFragment;

    private FavoritesPresenter mFavoritesPresenter;

    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initFragments(savedInstanceState);

        if (savedInstanceState != null) {
            int id = savedInstanceState.getInt(KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID, R.id.nav_timeline);
            switch (id) {
                case R.id.nav_timeline:
                    showFragment(mTimelineFragment);
                    break;
                case R.id.nav_favorites:
                    showFragment(mFavoritesFragment);
                    break;
                case R.id.nav_info:
                    showFragment(mInfoFragment);
                    break;
            }
        } else {
            showFragment(mTimelineFragment);
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener((menuItem -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.nav_timeline:
                    showFragment(mTimelineFragment);
                break;

                case R.id.nav_favorites:
                    showFragment(mFavoritesFragment);
                    break;

                case R.id.nav_info:
                    showFragment(mInfoFragment);
                    break;

                default:
                    break;

            }
            ft.commit();
            return true;
        }));

        // Start the caching service.
        startService(new Intent(MainActivity.this, CacheService.class));

    }

    private void initViews() {
        mBottomNavigationView = findViewById(R.id.bottom_nav);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID, mBottomNavigationView.getSelectedItemId());
        FragmentManager fm = getSupportFragmentManager();
        if (mTimelineFragment.isAdded()) {
            fm.putFragment(outState, TimelineFragment.class.getSimpleName(), mTimelineFragment);
        }
        if (mFavoritesFragment.isAdded()) {
            fm.putFragment(outState, FavoritesFragment.class.getSimpleName(), mFavoritesFragment);
        }
        if (mInfoFragment.isAdded()) {
            fm.putFragment(outState, InfoFragment.class.getSimpleName(), mInfoFragment);
        }
    }

    private void initFragments(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mTimelineFragment = TimelineFragment.newInstance();
            mInfoFragment = InfoFragment.newInstance();
            mFavoritesFragment = FavoritesFragment.newInstance();
        } else {
            mTimelineFragment = (TimelineFragment) fm.getFragment(savedInstanceState, TimelineFragment.class.getSimpleName());
            mInfoFragment = (InfoFragment) fm.getFragment(savedInstanceState, InfoFragment.class.getSimpleName());
            mFavoritesFragment = (FavoritesFragment) fm.getFragment(savedInstanceState, FavoritesFragment.class.getSimpleName());
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        if (fragment instanceof TimelineFragment) {
            if (!mTimelineFragment.isAdded()) {
                fm.beginTransaction()
                        .add(R.id.container, mTimelineFragment, TimelineFragment.class.getSimpleName())
                        .commit();
                if (mFavoritesPresenter == null) {
                    mFavoritesPresenter = new FavoritesPresenter(mFavoritesFragment);
                }
            }
            fm.beginTransaction()
                    .show(mTimelineFragment)
                    .hide(mInfoFragment)
                    .hide(mFavoritesFragment)
                    .commit();

        } else if (fragment instanceof InfoFragment) {
            if (!mInfoFragment.isAdded()) {
                fm.beginTransaction()
                        .add(R.id.container, mInfoFragment, InfoFragment.class.getSimpleName())
                        .commit();
            }
            fm.beginTransaction()
                    .show(mInfoFragment)
                    .hide(mTimelineFragment)
                    .hide(mFavoritesFragment)
                    .commit();
        } else if (fragment instanceof FavoritesFragment) {
            if (!mFavoritesFragment.isAdded()) {
                fm.beginTransaction()
                        .add(R.id.container, mFavoritesFragment, FavoritesFragment.class.getSimpleName())
                        .commit();
            }
            fm.beginTransaction()
                    .show(mFavoritesFragment)
                    .hide(mTimelineFragment)
                    .hide(mInfoFragment)
                    .commit();
        }
    }

}
