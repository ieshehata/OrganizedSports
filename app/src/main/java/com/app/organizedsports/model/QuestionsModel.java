package com.app.organizedsports.model;

import java.util.Date;

public class QuestionsModel {
    private String key;
    private String question = "";
    private String userKey;
    private Date createdAt;
    private UserModel fromUser;

    public QuestionsModel() {
    }

    public QuestionsModel(String key, String question, String userKey, Date createdAt, UserModel fromUser) {
        this.key = key;
        this.question = question;
        this.userKey = userKey;
        this.createdAt = createdAt;
        this.fromUser = fromUser;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UserModel getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserModel fromUser) {
        this.fromUser = fromUser;
    }
}
