package com.anotherdev.firebase.dynamic.links;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
interface FdlRequest {

    @SerializedName("dynamicLinkInfo")
    DynamicLinkInfo getDynamicLinkInfo();

    static ImmutableFdlRequest.Builder builder() {
        return ImmutableFdlRequest.builder();
    }


    @Value.Immutable
    @Value.Style(strictBuilder = true)
    @Gson.TypeAdapters
    interface DynamicLinkInfo {

        @SerializedName("domainUriPrefix")
        String getDomainUriPrefix();

        static ImmutableDynamicLinkInfo.Builder builder() {
            return ImmutableDynamicLinkInfo.builder();
        }
    }
}
