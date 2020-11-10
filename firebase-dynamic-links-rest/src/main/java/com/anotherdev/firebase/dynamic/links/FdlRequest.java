package com.anotherdev.firebase.dynamic.links;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
interface FdlRequest {

    @Nullable
    @SerializedName("dynamicLinkInfo")
    DynamicLinkInfo getDynamicLinkInfo();

    @Nullable
    @SerializedName("suffix")
    Suffix getSuffix();

    static ImmutableFdlRequest.Builder builder() {
        return ImmutableFdlRequest.builder();
    }


    @Value.Immutable
    @Value.Style(strictBuilder = true)
    @Gson.TypeAdapters
    interface DynamicLinkInfo {

        @SerializedName("domainUriPrefix")
        String getDomainUriPrefix();

        @SerializedName("link")
        String getLink();

        static ImmutableDynamicLinkInfo.Builder builder() {
            return ImmutableDynamicLinkInfo.builder();
        }
    }


    @Value.Immutable
    @Value.Style(strictBuilder = true)
    @Gson.TypeAdapters
    interface Suffix {

        @Value.Default
        @SerializedName("option")
        default Option getOption() {
            return Option.UNGUESSABLE;
        }

        static ImmutableSuffix.Builder builder() {
            return ImmutableSuffix.builder();
        }


        enum Option {
            UNGUESSABLE,
            SHORT
        }
    }
}
