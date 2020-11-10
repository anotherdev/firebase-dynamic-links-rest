package com.anotherdev.firebase.dynamic.links;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(
        strictBuilder = true
)
public interface DynamicLinkRest {

    DynamicLink.AndroidParameters.Builder getAndroidParametersBuilder();
    DynamicLink.SocialMetaTagParameters.Builder getSocialMetaTagParametersBuilder();
    DynamicLink.Builder getDynamicLinkBuilder();


    class Builder extends ImmutableDynamicLinkRest.Builder {

        @NonNull
        private final FirebaseDynamicLinksRest firebase;

        Builder() {
            this(FirebaseDynamicLinksRest.getInstance());
        }

        Builder(@NonNull FirebaseDynamicLinksRest firebase) {
            this.firebase = firebase;
        }

        @NonNull
        public Task<ShortDynamicLink> buildShortDynamicLink(int suffix) {
            return firebase.buildShortDynamicLink(suffix);
        }
    }
}
