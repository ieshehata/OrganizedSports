package organizesports.callbacks;


import com.app.organizesports.models.DataModel;

import java.util.ArrayList;

public interface DataCallback {
    void onSuccess(ArrayList<DataModel> exerciseTypes);
    void onFail(String error);
}
