package organizesports.callbacks;


import com.app.organizesports.models.ConversationModel;

import java.util.ArrayList;

public interface ConversationCallback {
    void onSuccess(ArrayList<ConversationModel> conversations);
    void onFail(String error);
}
