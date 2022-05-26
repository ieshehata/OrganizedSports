package organizesports.callbacks;


import com.app.organizesports.models.DayModel;

import java.util.ArrayList;

public interface DayCallback {
    void onSuccess(ArrayList<DayModel> days);
    void onFail(String error);
}
