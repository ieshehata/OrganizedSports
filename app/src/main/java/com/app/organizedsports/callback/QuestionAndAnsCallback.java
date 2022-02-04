package com.app.organizedsports.callback;

import com.app.organizedsports.model.QuestionAndAnsModel;

import java.util.ArrayList;

public interface QuestionAndAnsCallback {
    void onSuccess(ArrayList<QuestionAndAnsModel> questionAndAnss);

    void onFail(String error);
}




    