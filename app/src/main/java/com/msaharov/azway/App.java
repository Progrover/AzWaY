package com.msaharov.azway;

import android.app.Application;
import android.content.Context;

import com.msaharov.azway.models.AppwriteClient;
import com.yandex.mapkit.MapKitFactory;

import java.lang.ref.WeakReference;


public class App extends Application {
    private static WeakReference<Context> mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = new WeakReference<>(this);
        String MAPKIT_API_KEY = "7a312972-cba8-4046-9e7b-7d256c365156";
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        AppwriteClient.setClient(getApplicationContext());

    }
    public static Context getContext() {
        return mContext.get();
    }
}