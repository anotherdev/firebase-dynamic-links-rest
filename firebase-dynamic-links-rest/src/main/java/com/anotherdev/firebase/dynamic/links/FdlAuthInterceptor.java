package com.anotherdev.firebase.dynamic.links;

import android.content.Context;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class FdlAuthInterceptor implements Interceptor {

    final String apiKey;


    FdlAuthInterceptor(@NonNull Context context) {
        apiKey = context.getString(R.string.google_api_key);
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        HttpUrl newUrl = originalRequest.url()
                .newBuilder()
                .addQueryParameter("key", apiKey)
                .build();

        Request newRequest = originalRequest.newBuilder()
                .header("Content-Type", "application/json")
                .url(newUrl)
                .build();

        return chain.proceed(newRequest);
    }
}
