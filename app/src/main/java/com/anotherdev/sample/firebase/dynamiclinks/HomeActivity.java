package com.anotherdev.sample.firebase.dynamiclinks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.anotherdev.sample.firebase.dynamiclinks.ui.main.HomeFragment;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import io.branch.referral.Branch;

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
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Branch.sessionBuilder(this)
                .withCallback(branchReferralInitListener)
                .withData(intent != null ? intent.getData() : null)
                .init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        registerReceiveDynamicLinks("onNewIntent");

        setIntent(intent);
        Branch.sessionBuilder(this)
                .withCallback(branchReferralInitListener)
                .reInit();
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

        Intent intent = getIntent();
        Log.e(TAG, "intent: " + intent);
        Log.e(TAG, "extras: " + (intent != null ? intent.getExtras() : "NULL"));
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            for (String key : extras.keySet()) {
                Log.e(TAG, "key: " + key + ": " + extras.get(key));
            }
        }
    }


    private final Branch.BranchReferralInitListener branchReferralInitListener = (referringParams, error) -> {
        Log.e(TAG, "Branch referringParams: " + referringParams);
        Log.e(TAG, "Branch error: " + error);
    };
}