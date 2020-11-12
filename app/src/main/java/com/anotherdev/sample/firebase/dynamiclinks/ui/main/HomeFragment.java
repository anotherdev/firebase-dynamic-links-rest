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

import com.anotherdev.firebase.dynamic.links.FirebaseDynamicLinksRest;
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

            DynamicLink.AndroidParameters.Builder paramBuilder = new DynamicLink.AndroidParameters.Builder()
                    .setFallbackUrl(Uri.parse("https://authrest.dre.agconnect.link/FjRw"));

            DynamicLink.SocialMetaTagParameters.Builder socialBuilder = new DynamicLink.SocialMetaTagParameters.Builder()
                    .setTitle("Social Title")
                    .setDescription("Social Description")
                    .setImageUrl(Uri.parse("http://via.placeholder.com/150?text=Social"));

            DynamicLink.Builder linkBuilder = FirebaseDynamicLinks.getInstance()
                    .createDynamicLink()
                    .setLink(Uri.parse("https://www.example.com"))
                    .setDomainUriPrefix("https://huaweidtse.page.link")
                    .setAndroidParameters(paramBuilder.build())
                    .setSocialMetaTagParameters(socialBuilder.build());

            FirebaseDynamicLinksRest.getInstance()
                    .createDynamicLink()
                    .androidParametersBuilder(paramBuilder)
                    .socialMetaTagParametersBuilder(socialBuilder)
                    .dynamicLinkBuilder(linkBuilder)
                    .buildShortDynamicLink(ShortDynamicLink.Suffix.UNGUESSABLE)
            //linkBuilder.buildShortDynamicLink(ShortDynamicLink.Suffix.UNGUESSABLE)
                    .addOnSuccessListener(link -> {
                        Log.i(TAG, "onSuccess: getPreviewLink: " + link.getPreviewLink());
                        Log.i(TAG, "onSuccess: getShortLink: " + link.getShortLink());
                        for (ShortDynamicLink.Warning warning : link.getWarnings()) {
                            Log.w(TAG, "onSuccess: warning: " + warning.getMessage());
                        }

                        String resultLink = String.format("link: %s\n\npreview: %s\n\n", link.getShortLink(), link.getPreviewLink());
                        StringBuilder text = new StringBuilder(resultLink);
                        for (ShortDynamicLink.Warning w : link.getWarnings()) {
                            text.append(String.format("code: %s\n", w.getCode()));
                            text.append(String.format("message: %s\n\n", w.getMessage()));
                        }
                        linkTextView.setText(text);
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