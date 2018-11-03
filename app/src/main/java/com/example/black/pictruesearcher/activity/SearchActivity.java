package com.example.black.pictruesearcher.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.black.pictruesearcher.R;
import com.example.black.pictruesearcher.fragment.SearchFragment;
import com.example.black.pictruesearcher.util.UrlManager;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("검색화면");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        searchIntent(intent);
    }

    private void searchIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String keyword = intent.getStringExtra(SearchManager.QUERY);

            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(UrlManager.PREF_SEARCH_QUERY, keyword).commit();
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.activity_search_fragment);
            if (fragment != null) {
                ((SearchFragment) fragment).refresh();
            }
        }
    }
}