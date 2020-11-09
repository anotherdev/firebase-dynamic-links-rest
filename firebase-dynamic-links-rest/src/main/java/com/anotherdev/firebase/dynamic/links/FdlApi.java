package com.anotherdev.firebase.dynamic.links;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface FdlApi {

    @POST("https://firebasedynamiclinks.googleapis.com/v1/shortLinks")
    Call<String> shortLinks(@Body String request);
}
