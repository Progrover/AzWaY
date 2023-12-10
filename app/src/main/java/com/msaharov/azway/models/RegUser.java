package com.msaharov.azway.models;

import androidx.navigation.NavType;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class RegUser implements Serializable {

    public RegUser(String name, int sex, String email, String emailFromPhone, String phoneNumber, Calendar birthday, String password) {
        this.birthday = birthday;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailFromPhone = emailFromPhone;
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
    private String emailFromPhone;
    private String phoneNumber;
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

    public String getEmailFromPhone() {
        return emailFromPhone;
    }

    public void setEmailFromPhone(String emailFromPhone) {
        this.emailFromPhone = emailFromPhone;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
