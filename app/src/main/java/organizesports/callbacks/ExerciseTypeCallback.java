package organizesports.callbacks;


import com.app.organizesports.models.ExerciseTypeModel;

import java.util.ArrayList;

public interface ExerciseTypeCallback {
    void onSuccess(ArrayList<ExerciseTypeModel> exerciseTypes);
    void onFail(String error);
}
