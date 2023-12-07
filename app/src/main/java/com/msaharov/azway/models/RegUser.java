package com.msaharov.azway.models;

import androidx.navigation.NavType;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class RegUser implements Serializable {

    public RegUser(String name, int sex, String email, String email_required, String phone_number, Calendar birthday, String password) {
        this.birthday = birthday;
        this.password = password;
        this.name = name;
        this.phone_number = phone_number;
        this.email_required = email_required;
        this.email = email;
        this.sex = sex;

    }

    private String name;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    private int sex;
    private String email;
    private String email_required;
    private String phone_number;
    private Calendar birthday;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail_required() {
        return email_required;
    }

    public void setEmail_required(String email_required) {
        this.email_required = email_required;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
