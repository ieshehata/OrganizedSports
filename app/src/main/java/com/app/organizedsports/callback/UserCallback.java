package com.app.organizedsports.callback;

import com.app.organizedsports.model.UserModel;

import java.util.ArrayList;

public interface UserCallback {
    void onSuccess(ArrayList<UserModel> users);

    void onFail(String error);
}




    