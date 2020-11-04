package com.anotherdev.sample.firebase.dynamiclinks.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class VipUtil {

    public static boolean isGooglePlayServicesAvailable(@NonNull Context context) {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
                == ConnectionResult.SUCCESS;
    }

    private VipUtil() {}
}
