package organizesports.controllers;


import androidx.annotation.NonNull;

import com.app.organizesports.callbacks.DataCallback;
import com.app.organizesports.models.DataModel;
import com.app.organizesports.utils.SharedData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataController {
    private String node = "Data";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<DataModel> data = new ArrayList<>();

    public void save(final DataModel model, final DataCallback callback) {
        if(model.getKey() == null || model.getKey().equals("")){
            model.setKey(myRef.push().getKey());
        }

        myRef = database.getReference(node + "/" + model.getKey());
        myRef.setValue(model)
                .addOnSuccessListener(aVoid -> {
                    data.add(model);
                    callback.onSuccess(data);
                })
                .addOnFailureListener(e -> callback.onFail(e.toString()));
    }


    public void getData(final DataCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataModel model = snapshot.getValue(DataModel.class);
                    data.add(model);
                }
                callback.onSuccess(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getExercisesAlways(final DataCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataModel model = snapshot.getValue(DataModel.class);
                    if(model.getType() == 1 && SharedData.chosenExerciseType != null && model.getExerciseKey().equals(SharedData.chosenExerciseType.getKey())) {
                        data.add(model);
                    }
                }
                callback.onSuccess(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getSupplementsAlways(final DataCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataModel model = snapshot.getValue(DataModel.class);
                    if(model.getType() == 2) {
                        data.add(model);
                    }
                }
                callback.onSuccess(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getRecipesAlways(final DataCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataModel model = snapshot.getValue(DataModel.class);
                    if(model.getType() == 3) {
                        data.add(model);
                    }
                }
                callback.onSuccess(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getDataAlways(final DataCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataModel model = snapshot.getValue(DataModel.class);
                    data.add(model);
                }
                callback.onSuccess(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getDataByKey(final String key, final DataCallback callback){
        Query query = myRef.orderByChild("key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataModel model = snapshot.getValue(DataModel.class);
                    if(model.getKey().equals(key)) {
                        data.add(model);
                    }
                }
                callback.onSuccess(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void delete(DataModel station,final DataCallback callback) {
        myRef = database.getReference(node+"/"+station.getKey());
        myRef.removeValue()
                .addOnFailureListener(e -> callback.onFail(e.toString()))
                .addOnSuccessListener(aVoid -> {
                    data = new ArrayList<>();
                    callback.onSuccess(data);
                });
    }

}
