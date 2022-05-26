package organizesports.controllers;


import androidx.annotation.NonNull;

import com.app.organizesports.callbacks.ExerciseTypeCallback;
import com.app.organizesports.models.ExerciseTypeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExerciseTypeController {
    private String node = "ExerciseTypes";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<ExerciseTypeModel> exerciseTypes = new ArrayList<>();

    public void save(final ExerciseTypeModel model, final ExerciseTypeCallback callback) {
        if(model.getKey() == null || model.getKey().equals("")){
            model.setKey(myRef.push().getKey());
        }

        myRef = database.getReference(node + "/" + model.getKey());
        myRef.setValue(model)
                .addOnSuccessListener(aVoid -> {
                    exerciseTypes.add(model);
                    callback.onSuccess(exerciseTypes);
                })
                .addOnFailureListener(e -> callback.onFail(e.toString()));
    }


    public void getExerciseTypes(final ExerciseTypeCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exerciseTypes = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ExerciseTypeModel model = snapshot.getValue(ExerciseTypeModel.class);
                    exerciseTypes.add(model);
                }
                callback.onSuccess(exerciseTypes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getExerciseTypesAlways(final ExerciseTypeCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exerciseTypes = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ExerciseTypeModel model = snapshot.getValue(ExerciseTypeModel.class);
                    exerciseTypes.add(model);
                }
                callback.onSuccess(exerciseTypes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getUserByKey(final String key, final ExerciseTypeCallback callback){
        Query query = myRef.orderByChild("key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exerciseTypes = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ExerciseTypeModel model = snapshot.getValue(ExerciseTypeModel.class);
                    if(model.getKey().equals(key)) {
                        exerciseTypes.add(model);
                    }
                }
                callback.onSuccess(exerciseTypes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void delete(ExerciseTypeModel station,final ExerciseTypeCallback callback) {
        myRef = database.getReference(node+"/"+station.getKey());
        myRef.removeValue()
                .addOnFailureListener(e -> callback.onFail(e.toString()))
                .addOnSuccessListener(aVoid -> {
                    exerciseTypes = new ArrayList<>();
                    callback.onSuccess(exerciseTypes);
                });
    }

}
