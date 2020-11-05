package com.anotherdev.sample.firebase.dynamiclinks;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.anotherdev.sample.firebase.dynamiclinks.ui.main.HomeFragment;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getName();

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

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(pendingDynamicLinkData -> {
                    Log.i(TAG, String.valueOf(pendingDynamicLinkData));
                    if (pendingDynamicLinkData != null) {
                        Log.i(TAG, String.valueOf(pendingDynamicLinkData.getLink()));
                        Log.i(TAG, String.valueOf(pendingDynamicLinkData.getClickTimestamp()));
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, e.getMessage(), e));
    }
}