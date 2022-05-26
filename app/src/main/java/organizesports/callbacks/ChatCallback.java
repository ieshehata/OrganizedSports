package organizesports.callbacks;


import com.app.organizesports.models.ChatModel;

import java.util.ArrayList;

public interface ChatCallback {
    void onSuccess(ArrayList<ChatModel> chats);
    void onFail(String error);
}
