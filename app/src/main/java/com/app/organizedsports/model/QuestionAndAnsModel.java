package com.app.organizedsports.model;

import java.util.Date;

public class QuestionAndAnsModel {
    private String key;
    private String questionKey;
    private String userKey;
    private Date createdAt;
    private UserModel fromUser;
    private String ans = "";

    public QuestionAndAnsModel() {
    }

    public QuestionAndAnsModel(String key, String questionKey, String userKey, Date createdAt, UserModel fromUser, String ans) {
        this.key = key;
        this.questionKey = questionKey;
        this.userKey = userKey;
        this.createdAt = createdAt;
        this.fromUser = fromUser;
        this.ans = ans;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQuestionKey() {
        return questionKey;
    }

    public void setQuestionKey(String questionKey) {
        this.questionKey = questionKey;
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

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}
