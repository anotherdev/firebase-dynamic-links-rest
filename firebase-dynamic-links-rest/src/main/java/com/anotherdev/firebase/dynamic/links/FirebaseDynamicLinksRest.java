package com.anotherdev.firebase.dynamic.links;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.anotherdev.firebase.dynamic.links.FdlRequest.DynamicLinkInfo;
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

    public DynamicLinkRest.Builder createDynamicLink() {
        return new DynamicLinkRest.Builder(this);
    }

    @NonNull
    Task<ShortDynamicLink> buildShortDynamicLink(
            DynamicLink.AndroidParameters.Builder android,
            DynamicLink.SocialMetaTagParameters.Builder social,
            DynamicLink.Builder linkBuilder,
            int suffix) {
        final Context context = firebaseApp.getApplicationContext();
        TaskCompletionSource<ShortDynamicLink> source = new TaskCompletionSource<>();

        DynamicLinkInfo.AndroidInfo androidInfo = ImmutableAndroidInfo.builder()
                .androidPackageName(context.getPackageName())
                .build();

        DynamicLinkInfo dynamicLinkInfo = ImmutableDynamicLinkInfo.builder()
                .domainUriPrefix(linkBuilder.getDomainUriPrefix())
                .link(linkBuilder.getLink().toString())
                .androidInfo(androidInfo)
                .build();

        FdlRequest request = ImmutableFdlRequest.builder()
                .dynamicLinkInfo(dynamicLinkInfo)
                .suffix(ImmutableSuffix.builder()
                        .option(FdlRequest.Suffix.Option.from(suffix))
                        .build())
                .build();

        getApi(context)
                .shortLinks(request)
                .enqueue(new Callback<FdlResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<FdlResponse> call, @NonNull Response<FdlResponse> httpResponse) {
                        Log.i(TAG, httpResponse.toString());
                        FdlResponse response = httpResponse.body();
                        if (httpResponse.isSuccessful() && response != null) {
                            source.trySetResult(new FdlShortDynamicLink(response));
                        } else {
                            String msg = String.format("Error: %s message: %s body: %s", httpResponse.code(), httpResponse.message(), response);
                            source.trySetException(new Exception(msg));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FdlResponse> call, @NonNull Throwable e) {
                        Log.e(TAG, e.getMessage(), e);
                        source.trySetException(new Exception(e));
                    }
                });
        return source.getTask();
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

            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(new GsonAdaptersFdlRequest())
                    .registerTypeAdapterFactory(new GsonAdaptersFdlResponse())
                    .create();

            api = new Retrofit.Builder()
                    .baseUrl(FdlApi.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(FdlApi.class);
        }
        return api;
    }
}
