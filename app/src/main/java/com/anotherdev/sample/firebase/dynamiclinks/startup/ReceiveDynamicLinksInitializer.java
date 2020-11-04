package com.anotherdev.sample.firebase.dynamiclinks.startup;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import java.util.Collections;
import java.util.List;

public class ReceiveDynamicLinksInitializer implements Initializer<Object> {

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }

    @NonNull
    @Override
    public Object create(@NonNull Context context) {
        return context;
    }
}
