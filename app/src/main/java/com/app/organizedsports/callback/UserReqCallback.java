package com.app.organizedsports.callback;

import com.app.organizedsports.model.UsersReqModel;

import java.util.ArrayList;

public interface UserReqCallback {
    void onSuccess(ArrayList<UsersReqModel> userReqs);

    void onFail(String error);
}




    