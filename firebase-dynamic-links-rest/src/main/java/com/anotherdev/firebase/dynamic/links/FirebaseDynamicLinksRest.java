package com.anotherdev.firebase.dynamic.links;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.firebase.FirebaseApp;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseDynamicLinksRest {

    private static FdlApi api;

    @NonNull
    private final FirebaseApp firebaseApp;


    public static FirebaseDynamicLinksRest getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    public static FirebaseDynamicLinksRest getInstance(@NonNull FirebaseApp firebaseApp) {
        return new FirebaseDynamicLinksRest(FirebaseApp.getInstance());
    }

    private FirebaseDynamicLinksRest(@NonNull FirebaseApp firebaseApp) {
        this.firebaseApp = firebaseApp;
    }

    @NonNull
    private FdlApi getApi(@NonNull Context context) {
        if (api == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ChuckerInterceptor.Builder(context).build())
                    .addInterceptor(new FdlAuthInterceptor(context))
                    .build();

            api = new Retrofit.Builder()
                    .baseUrl(FdlApi.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(FdlApi.class);
        }
        return api;
    }
}
