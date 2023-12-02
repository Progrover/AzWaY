package com.msaharov.azway;

import android.content.Context;

import io.appwrite.Client;
public class AppwriteClient {
    public static Client getClient() {
        return client;
    }

    public static void setClient(Context context) {
        AppwriteClient.client = new Client(context)
                .setEndpoint("http://89.253.255.107/v1")
                .setProject("65634e0800b7e073a171")
                .setSelfSigned(true);
    }

    private static Client client;

    private AppwriteClient() {}

}
