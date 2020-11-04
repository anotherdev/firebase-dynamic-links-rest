package com.anotherdev.sample.firebase.dynamiclinks.ui.main;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anotherdev.sample.firebase.dynamiclinks.R;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getName();

    Button createLinkButton;
    TextView linkTextView;

    private HomeViewModel homeViewModel;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createLinkButton = view.findViewById(R.id.create_link_button);
        linkTextView = view.findViewById(R.id.link_textview);

        createLinkButton.setOnClickListener(button -> {
            linkTextView.setText(null);

            DynamicLink.AndroidParameters.Builder paramBuilder = new DynamicLink.AndroidParameters.Builder();

            DynamicLink.SocialMetaTagParameters.Builder socialBuilder = new DynamicLink.SocialMetaTagParameters.Builder()
                    .setTitle("Social Title")
                    .setDescription("Social Description")
                    .setImageUrl(Uri.parse("http://via.placeholder.com/150?text=Social"));

            DynamicLink.Builder linkBuilder = FirebaseDynamicLinks.getInstance()
                    .createDynamicLink()
                    .setLink(Uri.parse("https://www.example.com"))
                    .setDomainUriPrefix("https://huaweidtse.page.link/")
                    .setAndroidParameters(paramBuilder.build())
                    .setSocialMetaTagParameters(socialBuilder.build());

            linkBuilder.buildShortDynamicLink(ShortDynamicLink.Suffix.UNGUESSABLE)
                    .addOnSuccessListener(link -> {
                        Log.e(TAG, "onSuccess: " + link);
                        linkTextView.setText(String.valueOf(link.getShortLink()));
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, e.getMessage(), e);
                        linkTextView.setText(e.getMessage());
                    });
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }
}