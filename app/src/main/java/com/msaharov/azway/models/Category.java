package com.msaharov.azway.models;

import com.google.gson.internal.LinkedTreeMap;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Category {
    private String name;
    private String pinUrl;

    public Category(String name, String pinUrl) {
        this.name = name;
        this.pinUrl = pinUrl;
    }
    public Category(@Nullable Map<?, ?> map){
        this.name = (String) map.get("name");
        this.pinUrl = (String) map.get("pin_url");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinUrl() {
        return pinUrl;
    }

    public void setPinUrl(String pinUrl) {
        this.pinUrl = pinUrl;
    }
}
