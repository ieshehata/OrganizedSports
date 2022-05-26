package organizesports.callbacks;


import com.app.organizesports.models.UserModel;

import java.util.ArrayList;

public interface UserCallback {
    void onSuccess(ArrayList<UserModel> users);
    void onFail(String error);
}
