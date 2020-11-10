package com.anotherdev.firebase.dynamic.links;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.firebase.FirebaseApp;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BASIC;
import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public class FirebaseDynamicLinksRest {

    private static final String TAG = FirebaseDynamicLinksRest.class.getName();

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
            final boolean isDebugBuild = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ChuckerInterceptor.Builder(context).build())
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(isDebugBuild ? BODY : BASIC))
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
