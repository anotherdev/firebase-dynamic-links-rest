package com.anotherdev.firebase.dynamic.links;

import androidx.annotation.Nullable;

import com.google.firebase.dynamiclinks.ShortDynamicLink;
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


    @Value.Immutable
    @Value.Style(strictBuilder = true)
    @Gson.TypeAdapters
    interface DynamicLinkInfo {

        @SerializedName("domainUriPrefix")
        String getDomainUriPrefix();

        @SerializedName("link")
        String getLink();

        @Nullable
        @SerializedName("androidInfo")
        AndroidInfo getAndroidInfo();


        @Value.Immutable
        @Value.Style(strictBuilder = true)
        @Gson.TypeAdapters
        interface AndroidInfo {

            @SerializedName("androidPackageName")
            String getAndroidPackageName();
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


        enum Option {
            UNGUESSABLE (ShortDynamicLink.Suffix.UNGUESSABLE),
            SHORT (ShortDynamicLink.Suffix.SHORT);

            final int suffixOption;

            Option(final int suffixOption) {
                this.suffixOption = suffixOption;
            }

            static Option from(final int suffixOption) {
                for (Option o : Option.values()) {
                    if (o.suffixOption == suffixOption) {
                        return o;
                    }
                }
                return UNGUESSABLE;
            }
        }
    }
}
