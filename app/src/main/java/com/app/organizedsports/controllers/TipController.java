package com.app.organizedsports.controllers;

import androidx.annotation.NonNull;

import com.app.organizedsports.callback.TipCallback;
import com.app.organizedsports.model.TipsModel;
import com.app.organizedsports.model.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TipController {
    private String node = "Tips";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<TipsModel> tips = new ArrayList<>();

    public void save(final TipsModel model, final TipCallback callback) {
        if(model.getKey() == null || model.getKey().equals("")){
            model.setKey(myRef.push().getKey());
        }

        myRef = database.getReference(node + "/" + model.getKey());
        myRef.setValue(model).addOnSuccessListener(aVoid -> {
            tips.add(model);
            callback.onSuccess(tips);
        })
                .addOnFailureListener(e -> callback.onFail(e.toString()));
    }

    public void getTips(final TipCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tips = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TipsModel model = snapshot.getValue(TipsModel.class);
                    tips.add(model);
                }
                callback.onSuccess(tips);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getTipsAlways(final TipCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tips = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TipsModel model = snapshot.getValue(TipsModel.class);
                    tips.add(model);
                }
                callback.onSuccess(tips);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getTipsByKey(final String key, final TipCallback callback){
        Query query = myRef.orderByChild("key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tips = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TipsModel model = snapshot.getValue(TipsModel.class);
                    if(model.getKey().equals(key)) {
                        tips.add(model);
                    }
                }
                callback.onSuccess(tips);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void delete(TipsModel rate,final TipCallback callback) {
        myRef = database.getReference(node+"/"+rate.getKey());
        myRef.removeValue()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFail(e.toString());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tips = new ArrayList<>();
                        callback.onSuccess(tips);
                    }
                });
    }

    public void newTip( String tip, final TipCallback callback) {
        TipsModel model = new TipsModel();
        model.setTips(tip);

        save(model, new TipCallback() {
            @Override
            public void onSuccess(ArrayList<TipsModel> tasks) {
                callback.onSuccess(tasks);
            }
            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }
}
