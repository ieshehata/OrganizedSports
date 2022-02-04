package com.app.organizedsports.controllers;

import androidx.annotation.NonNull;

import com.app.organizedsports.callback.QuestionsCallback;
import com.app.organizedsports.model.QuestionsModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionsController {
    private String node = "Questions";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<QuestionsModel> questions = new ArrayList<>();

    public void save(final QuestionsModel model, final QuestionsCallback callback) {
        if(model.getKey() == null || model.getKey().equals("")){
            model.setKey(myRef.push().getKey());
        }

        myRef = database.getReference(node + "/" + model.getKey());
        myRef.setValue(model)
                .addOnSuccessListener(aVoid -> {
                    questions.add(model);
                    callback.onSuccess(questions);
                })
                .addOnFailureListener(e -> callback.onFail(e.toString()));
    }

    public void getQuestions(final QuestionsCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questions = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    QuestionsModel model = snapshot.getValue(QuestionsModel.class);
                    questions.add(model);
                }
                callback.onSuccess(questions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getQuestionsAlways(final QuestionsCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questions = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    QuestionsModel model = snapshot.getValue(QuestionsModel.class);
                    questions.add(model);
                }
                callback.onSuccess(questions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getQuestionsByKey(final String key, final QuestionsCallback callback){
        Query query = myRef.orderByChild("key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questions = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    QuestionsModel model = snapshot.getValue(QuestionsModel.class);
                    if(model.getKey().equals(key)) {
                        questions.add(model);
                    }
                }
                callback.onSuccess(questions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void delete(QuestionsModel model,final QuestionsCallback callback) {
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
                        questions = new ArrayList<>();
                        callback.onSuccess(questions);
                    }
                });
    }

    public void newQuestions(String q, final QuestionsCallback callback) {
        QuestionsModel model = new QuestionsModel();
        model.setQuestion(q);
        save(model, new QuestionsCallback() {
            @Override
            public void onSuccess(ArrayList<QuestionsModel> tasks) {
                callback.onSuccess(tasks);
            }
            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }
}
