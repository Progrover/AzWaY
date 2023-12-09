package com.msaharov.azway.models;

import com.google.gson.internal.LinkedTreeMap;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Mark implements Serializable {
    private String uuid;

    private Double lon;
    private Double lat;
    private String name;
    private String desc;
    private Category category;
    private ArrayList<String> images = new ArrayList<>();



    public Mark(@Nullable Map<?, ?> map){
        this.lat = (Double) map.get("lat");
        this.lon = (Double) map.get("lon");
        this.name = (String) map.get("name");
        this.desc = (String) map.get("desc");
        this.uuid = (String) map.get("$id");
        this.images = (ArrayList<String>) map.get("images");

        try {
            category = new Category((LinkedTreeMap<String, Object>) map.get("category"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
