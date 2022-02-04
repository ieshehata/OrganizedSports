package com.app.organizedsports.model;

import java.util.Date;

public class TipsModel {
    private String key;
    private String tips;
    private Date createdAt;

    public TipsModel() {
    }

    public TipsModel(String key, String tips, Date createdAt) {
        this.key = key;
        this.tips = tips;
        this.createdAt = createdAt;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
