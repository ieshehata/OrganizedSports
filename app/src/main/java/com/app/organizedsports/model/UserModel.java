package com.app.organizedsports.model;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.Date;

public class UserModel {
    private String key;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String image;
    private int gender; // 1->male, 2->female
    private boolean isPublic = true;
    private int userType = 0;
    private Date createdAt;
    private int activated; // -1->Blocked, 0->waiting, 1->Active
    private int state; //-1>Blocked, 0->waiting, 1->Allowed
    private String fcmToken = "";


    public boolean validate() {
        return  (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) &&
                !TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phone);
    }


    public UserModel() {
    }

    public UserModel(String phone, String password, int userType) {
        this.phone = phone;
        this.password = password;
        this.userType = userType;
    }

    public UserModel(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }


    public UserModel(String key, String name, String email, String phone, String password, String image, int gender, boolean isPublic, int userType, Date createdAt, int activated, int state, String fcmToken) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.gender = gender;
        this.isPublic = isPublic;
        this.userType = userType;
        this.createdAt = createdAt;
        this.activated = activated;
        this.state = state;
        this.fcmToken = fcmToken;
    }

    public UserModel(String key, String name, String email, String phone, String password, String image, int activated) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.activated = activated;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
