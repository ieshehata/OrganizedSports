package organizesports.models;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class UserModel {
    private String key;
    private String name;
    private String pass;
    private String email;
    private String phone;
    private String profileImage = "";
    private int gender; // 1->male, 2->female
    private boolean isPublic = true;
    private int userType = 0;
    private Date createdAt;
    private int activated; // -1->Blocked, 0->waiting, 1->Active
    private int state; //-1>Blocked, 0->waiting, 1->Allowed
    private String fcmToken = "";
    private ArrayList<DayModel> days = new ArrayList<>();

    public UserModel() {
    }

    public UserModel(boolean isAdmin) {
        this.key = "Admin";
        this.name = "Admin";
        this.email = "Admin";
        this.userType = 1;
    }

    public UserModel(String pass, String phone) {
        this.pass = pass;
        this.phone = phone;
    }

    public UserModel(String phone, String pass, int userType) {
        this.phone = phone;
        this.pass = pass;
        this.userType = userType;
    }

    public UserModel(String key, String name, String pass, String email, String phone, String profileImage, int gender, boolean isPublic, int userType, Date createdAt, int activated, int state) {
        this.key = key;
        this.name = name;
        this.pass = pass;
        this.email = email;
        this.phone = phone;
        this.profileImage = profileImage;
        this.gender = gender;
        this.isPublic = isPublic;
        this.userType = userType;
        this.createdAt = createdAt;
        this.activated = activated;
        this.state = state;
    }

    public UserHeaderModel toHeader() {
        return new UserHeaderModel(this);
    }

    public boolean validate() {
        return  (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) &&
                !TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(phone) && userType != 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || UserHeaderModel.class != o.getClass()) return false;
        if(UserHeaderModel.class == o.getClass()) {
            UserHeaderModel userModel = (UserHeaderModel) o;
            return key.equals(userModel.getKey());
        }else {
            UserModel userModel = (UserModel) o;
            return key.equals(userModel.key);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    public ArrayList<DayModel> getDays() {
        return days;
    }

    public void setDays(ArrayList<DayModel> days) {
        this.days = days;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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

    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}