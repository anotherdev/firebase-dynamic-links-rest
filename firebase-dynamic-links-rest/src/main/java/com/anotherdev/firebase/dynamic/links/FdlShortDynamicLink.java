package com.anotherdev.firebase.dynamic.links;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.List;
import java.util.stream.Collectors;

class FdlShortDynamicLink implements ShortDynamicLink {

    @NonNull
    final FdlResponse response;


    FdlShortDynamicLink(@NonNull FdlResponse response) {
        this.response = response;
    }

    @Nullable
    @Override
    public Uri getShortLink() {
        return Uri.parse(response.getShortLink());
    }

    @Nullable
    @Override
    public Uri getPreviewLink() {
        return Uri.parse(response.getPreviewLink());
    }

    @NonNull
    @Override
    public List<Warning> getWarnings() {
        return response.getWarnings()
                .stream()
                .map(warning -> new Warning() {
                    @Nullable
                    @Override
                    public String getCode() {
                        return warning.getWarningCode();
                    }

                    @Nullable
                    @Override
                    public String getMessage() {
                        return warning.getWarningMessage();
                    }
                })
                .collect(Collectors.toList());
    }
}
