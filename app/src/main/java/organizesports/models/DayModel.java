package organizesports.models;

import java.util.Date;

public class DayModel {
    private String key;
    private String userKey;
    private Date day;
    private int minOfTraining;
    private double weight;
    private double height;
    private double bmi;
    private int waterCups = 0;
    private double walkedDistance = 0.0;



    public DayModel() {
    }

    public double getWalkedDistance() {
        return walkedDistance;
    }

    public void setWalkedDistance(double walkedDistance) {
        this.walkedDistance = walkedDistance;
    }

    public int getWaterCups() {
        return waterCups;
    }

    public void setWaterCups(int waterCups) {
        this.waterCups = waterCups;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public int getMinOfTraining() {
        return minOfTraining;
    }

    public void setMinOfTraining(int minOfTraining) {
        this.minOfTraining = minOfTraining;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }
}
