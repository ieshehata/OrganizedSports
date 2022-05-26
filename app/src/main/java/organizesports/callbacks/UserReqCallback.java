package organizesports.callbacks;


import com.app.organizesports.models.UserReqModel;

import java.util.ArrayList;

public interface UserReqCallback {
    void onSuccess(ArrayList<UserReqModel> userReqs);

    void onFail(String error);
}




    