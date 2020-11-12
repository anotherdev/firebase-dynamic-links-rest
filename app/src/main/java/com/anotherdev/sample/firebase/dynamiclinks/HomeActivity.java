package com.anotherdev.sample.firebase.dynamiclinks;

import android.content.Intent;
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

        registerReceiveDynamicLinks("onCreate");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        registerReceiveDynamicLinks("onNewIntent");
    }

    private void registerReceiveDynamicLinks(String title) {
        Log.i(TAG, "registerReceiveDynamicLinks: " + title);
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(pendingDynamicLinkData -> {
                    Log.i(TAG, String.format("%s: %s", title, pendingDynamicLinkData));
                    if (pendingDynamicLinkData != null) {
                        Log.i(TAG, String.format("%s: %s", title, pendingDynamicLinkData.getLink()));
                        Log.i(TAG, String.format("%s: %s", title, pendingDynamicLinkData.getClickTimestamp()));
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, String.format("%s: %s", title, e.getMessage()), e));
    }
}