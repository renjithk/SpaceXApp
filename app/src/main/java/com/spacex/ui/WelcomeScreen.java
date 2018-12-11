package com.spacex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.spacex.R;

/**
 * SpaceX welcome screen
 *
 * Created by Renjith Kandanatt on 10/12/2018.
 */
public class WelcomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_Welcome);
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), RocketList.class);
                startActivity(intent);
                finish();
            }
        }, 400);
    }
}
