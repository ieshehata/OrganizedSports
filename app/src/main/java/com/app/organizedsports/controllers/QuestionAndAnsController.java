package com.app.organizedsports.controllers;

import androidx.annotation.NonNull;

import com.app.organizedsports.callback.QuestionAndAnsCallback;
import com.app.organizedsports.model.QuestionAndAnsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionAndAnsController {
    private String node = "QuestionAndAns";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<QuestionAndAnsModel> questionAndAns = new ArrayList<>();

    public void save(final QuestionAndAnsModel model, final QuestionAndAnsCallback callback) {
        if(model.getKey() == null || model.getKey().equals("")){
            model.setKey(myRef.push().getKey());
        }

        myRef = database.getReference(node + "/" + model.getKey());
        myRef.setValue(model)
                .addOnSuccessListener(aVoid -> {
                    questionAndAns.add(model);
                    callback.onSuccess(questionAndAns);
                })
                .addOnFailureListener(e -> callback.onFail(e.toString()));
    }

    public void getQuestionAndAns(final QuestionAndAnsCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionAndAns = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    QuestionAndAnsModel tip = snapshot.getValue(QuestionAndAnsModel.class);
                    questionAndAns.add(tip);
                }
                callback.onSuccess(questionAndAns);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getQuestionAndAnsAlways(final QuestionAndAnsCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionAndAns = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    QuestionAndAnsModel model = snapshot.getValue(QuestionAndAnsModel.class);
                    questionAndAns.add(model);
                }
                callback.onSuccess(questionAndAns);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getQuestionAndAnsByKey(final String key, final QuestionAndAnsCallback callback){
        Query query = myRef.orderByChild("key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionAndAns = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    QuestionAndAnsModel model = snapshot.getValue(QuestionAndAnsModel.class);
                    if(model.getKey().equals(key)) {
                        questionAndAns.add(model);
                    }
                }
                callback.onSuccess(questionAndAns);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void delete(QuestionAndAnsModel model,final QuestionAndAnsCallback callback) {
        myRef = database.getReference(node+"/"+model.getKey());
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
                        questionAndAns = new ArrayList<>();
                        callback.onSuccess(questionAndAns);
                    }
                });
    }

    public void newQuestionAndAns(String ans, final QuestionAndAnsCallback callback) {
        QuestionAndAnsModel model = new QuestionAndAnsModel();
        model.setAns(ans);
        save(model, new QuestionAndAnsCallback() {
            @Override
            public void onSuccess(ArrayList<QuestionAndAnsModel> tasks) {
                callback.onSuccess(tasks);
            }
            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }
}
