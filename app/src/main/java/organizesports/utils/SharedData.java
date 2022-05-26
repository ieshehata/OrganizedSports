package organizesports.utils;


import com.app.organizesports.models.ConversationModel;
import com.app.organizesports.models.DataModel;
import com.app.organizesports.models.DayModel;
import com.app.organizesports.models.ElementModel;
import com.app.organizesports.models.ExerciseTypeModel;
import com.app.organizesports.models.TipModel;
import com.app.organizesports.models.UserHeaderModel;
import com.app.organizesports.models.UserModel;

import java.util.ArrayList;

public class SharedData {
    public static boolean isTesting = false;

    public static int userType = 2; // 1->Admin, 2->user

    public static UserModel adminUser = new UserModel(true);
    public static UserModel currentUser;
    public static DayModel currentDay = new DayModel();

    public static UserModel stalkedUser;
    public static UserHeaderModel currentUserHeader;
    public static UserHeaderModel stalkedUserHeader;
    public static ConversationModel currentConversation;
    public static ExerciseTypeModel chosenExerciseType;
    public static DataModel chosenData;
    public static ElementModel chosenElement;
    public static int chosenElementIndex;
    public static int currentDataType = 1; //1->Exercise, 2->Supplements, 3->Recipes
    public static ArrayList<ElementModel> currentElements = new ArrayList<>();
    public static TipModel currentTip = new TipModel();


    public static String format = "EEE, d MMM yyyy hh:mm a";
    public static String formatTime = "hh:mm a";
    public static String formatDate = "dd/MM/yyyy";
    public static String formatDateTime = "dd/MM/yyyy hh:mm a";

    public static String imageUrl;

    public static final int NOTIFICATION_ID = 1303;
    public static final String PREF_KEY = "login";
    public static final String IS_USER_SAVED = "SAVED_USER";
    public static final String PHONE = "PHONE";
    public static final String PASS = "PASS";
}
