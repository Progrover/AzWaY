package com.msaharov.azway.models;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppwriteClient.setClient(getApplicationContext());
    }
}
