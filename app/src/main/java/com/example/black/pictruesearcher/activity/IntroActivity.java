package com.example.black.pictruesearcher.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.black.pictruesearcher.R;

public class IntroActivity extends AppCompatActivity {

    private static boolean firstStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        setTitle("인트로 화면");

        final Intent intent = new Intent(IntroActivity.this, SearchActivity.class);
        if(firstStart){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    firstStart = false;
                    next(intent);
                }
            }, 1300);
        } else {
            next(intent);
        }
    }

    private void next(Intent intent) {
        startActivity(intent);
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

}