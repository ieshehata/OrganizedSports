package com.app.organizedsports.model;

import java.util.Date;

public class UsersReqModel {
    private String key;
    private UserModel user;
    private int state; // 0->Unseen, 1->Accepted, -1->Rejected
    private Date createdAt;

    public UsersReqModel() {
    }

    public UsersReqModel(String key, UserModel user, int state, Date createdAt) {
        this.key = key;
        this.user = user;
        this.state = state;
        this.createdAt = createdAt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public static class  ClassNew {
        public void func() {
        }
    }

}
