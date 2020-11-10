package com.anotherdev.firebase.dynamic.links;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.List;

class FdlShortDynamicLink implements ShortDynamicLink {

    @NonNull
    final FdlResponse response;


    FdlShortDynamicLink(@NonNull FdlResponse response) {
        this.response = response;
    }

    @Nullable
    @Override
    public Uri getShortLink() {
        return null;
    }

    @Nullable
    @Override
    public Uri getPreviewLink() {
        return null;
    }

    @NonNull
    @Override
    public List<? extends Warning> getWarnings() {
        return null;
    }
}
