package com.app.organizedsports.callback;

import com.app.organizedsports.model.QuestionsModel;

import java.util.ArrayList;

public interface QuestionsCallback {
    void onSuccess(ArrayList<QuestionsModel> questionss);

    void onFail(String error);
}




    