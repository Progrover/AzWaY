package com.msaharov.azway.models;

import java.io.Serializable;

public class RoomObject implements Serializable {

    public RoomObject() {

    }

    public RoomObject(String owner_type, String object_type, String owner_id, String object_id) {
        this.object_type = object_type;
        this.owner_type = owner_type;
        this.object_id = object_id;
        this.owner_id = owner_id;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    private String object_type;

    private String owner_type;

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    private String owner_id;
    private String object_id;


    public String getOwner_type() {
        return owner_type;
    }

    public void setOwner_type(String owner_type) {
        this.owner_type = owner_type;
    }
}
