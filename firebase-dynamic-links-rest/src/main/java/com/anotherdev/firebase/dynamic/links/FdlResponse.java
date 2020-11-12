package com.anotherdev.firebase.dynamic.links;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
interface FdlResponse {

    @SerializedName("shortLink")
    String getShortLink();

    @SerializedName("previewLink")
    String getPreviewLink();

    @SerializedName("warning")
    List<Warning> getWarnings();


    @Value.Immutable
    @Value.Style(strictBuilder = true)
    @Gson.TypeAdapters
    interface Warning {

        @SerializedName("warningCode")
        String getWarningCode();

        @SerializedName("warningMessage")
        String getWarningMessage();
    }
}
