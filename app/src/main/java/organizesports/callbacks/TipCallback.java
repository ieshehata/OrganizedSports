package organizesports.callbacks;


import com.app.organizesports.models.TipModel;

import java.util.ArrayList;

public interface TipCallback {
    void onSuccess(ArrayList<TipModel> tips);
    void onFail(String error);
}
