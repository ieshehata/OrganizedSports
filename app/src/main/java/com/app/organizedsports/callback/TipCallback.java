package com.app.organizedsports.callback;

import com.app.organizedsports.model.TipsModel;

import java.util.ArrayList;

public interface TipCallback {
    void onSuccess(ArrayList<TipsModel> tips);

    void onFail(String error);
}




    