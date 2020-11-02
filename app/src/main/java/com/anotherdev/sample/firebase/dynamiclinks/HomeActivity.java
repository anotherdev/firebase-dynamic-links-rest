package com.anotherdev.sample.firebase.dynamiclinks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anotherdev.sample.firebase.dynamiclinks.ui.main.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, HomeFragment.newInstance())
                    .commitNow();
        }
    }
}