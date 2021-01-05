package com.anotherdev.sample.firebase.dynamiclinks.ui.main;

import android.content.Context;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anotherdev.firebase.dynamic.links.FirebaseDynamicLinksRest;
import com.anotherdev.sample.firebase.dynamiclinks.R;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getName();

    Button createFirebaseLinkButton;
    Button createBranchLinkButton;
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
        createFirebaseLinkButton = view.findViewById(R.id.create_firebase_button);
        createBranchLinkButton = view.findViewById(R.id.create_branch_button);
        linkTextView = view.findViewById(R.id.link_textview);

        createFirebaseLinkButton.setOnClickListener(button -> {
            linkTextView.setText(null);

            DynamicLink.AndroidParameters.Builder paramBuilder = new DynamicLink.AndroidParameters.Builder()
                    .setFallbackUrl(Uri.parse("https://authrest.dre.agconnect.link/FjRw"));

            DynamicLink.SocialMetaTagParameters.Builder socialBuilder = new DynamicLink.SocialMetaTagParameters.Builder()
                    .setTitle("Social Title")
                    .setDescription("Social Description")
                    .setImageUrl(Uri.parse("http://via.placeholder.com/150?text=Firebase"));

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

        createBranchLinkButton.setOnClickListener(button -> {
            linkTextView.setText(null);
            Context context = button.getContext();

            BranchUniversalObject buo = new BranchUniversalObject()
                    .setCanonicalIdentifier("content/123")
                    .setTitle("Buo Title")
                    .setContentDescription("Buo Content Description")
                    .setContentImageUrl("http://via.placeholder.com/150?text=Branch.io")
                    .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .setContentMetadata(new ContentMetadata().addCustomMetadata("key1", "value1"));

            buo.generateShortUrl(context, new LinkProperties(), (url, error) -> {
                Log.e(TAG, "Branch url: " + url);
                Log.e(TAG, "Branch error: " + error);
                if (error != null) {
                    linkTextView.setText(String.format("Branch.io error\ncode: %s\n message: %s", error.getErrorCode(), error.getMessage()));
                } else {
                    String text = String.format("Branch.io: url: %s\n\n%s",
                            url,
                            "https://petallink.app.link/static_ag_link");
                    linkTextView.setText(text);
                }
            });

            ShareSheetStyle ss = new ShareSheetStyle(context, "Check this out!", "This stuff is awesome: ")
                    .setCopyUrlStyle(ContextCompat.getDrawable(context, android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                    .setMoreOptionStyle(ContextCompat.getDrawable(context, android.R.drawable.ic_menu_search), "Show more")
                    .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                    .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                    .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                    .addPreferredSharingOption(SharingHelper.SHARE_WITH.HANGOUT)
                    .setAsFullWidthStyle(true)
                    .setSharingTitle("Share With");

            buo.showShareSheet(requireActivity(), new LinkProperties(), ss, null);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }
}