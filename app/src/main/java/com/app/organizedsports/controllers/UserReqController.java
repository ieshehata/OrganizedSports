package com.app.organizedsports.controllers;

import androidx.annotation.NonNull;

import com.app.organizedsports.callback.UserCallback;
import com.app.organizedsports.callback.UserReqCallback;
import com.app.organizedsports.model.UserModel;
import com.app.organizedsports.model.UsersReqModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserReqController {
    private String node = "RegistrationRequests";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<UsersReqModel> requests = new ArrayList<>();

    public void save(final UsersReqModel request, final UserReqCallback callback) {
        if(request.getKey() == null){
            request.setKey(myRef.push().getKey());
        }else if(request.getKey().equals("")){
            request.setKey(myRef.push().getKey());
        }
        myRef = database.getReference(node + "/" + request.getKey());
        myRef.setValue(request)
                .addOnSuccessListener(aVoid -> {
                    requests.add(request);
                    callback.onSuccess(requests);
                })
                .addOnFailureListener(e -> callback.onFail(e.toString()));
    }

    public void getRequests(final UserReqCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UsersReqModel request = snapshot.getValue(UsersReqModel.class);
                    requests.add(request);
                }
                callback.onSuccess(requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void geRequestsAlways(final UserReqCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UsersReqModel request = snapshot.getValue(UsersReqModel.class);
                    if(request.getState() == 0) {
                        requests.add(request);
                    }
                }
                callback.onSuccess(requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getRequestByKey(final String key, final UserReqCallback callback){
        Query query = myRef.orderByChild("key").equalTo(key);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UsersReqModel a = snapshot.getValue(UsersReqModel.class);
                    if(a.getKey().equals(key)) {
                        requests.add(a);
                    }
                }
                callback.onSuccess(requests);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void delete(UsersReqModel request, final UserReqCallback callback) {
        myRef = database.getReference(node+"/"+request.getKey());
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
                        requests = new ArrayList<>();
                        callback.onSuccess(requests);
                    }
                });
    }

    public void newRequest(UserModel user, final UserReqCallback callback) {
        final UsersReqModel request = new UsersReqModel();
        request.setUser(user);
        request.setState(0);

        save(request, new UserReqCallback() {
            @Override
            public void onSuccess(ArrayList<UsersReqModel> userReqs) {
                callback.onSuccess(requests);
            }

            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void responseOnRequest(UsersReqModel request, Boolean isAccepted, final UserReqCallback callback) {
        request.setState(isAccepted ? 1 : -1);
        save(request, new UserReqCallback() {
            @Override
            public void onSuccess(ArrayList<UsersReqModel> requests) {
                new UserController().getUserByKey(request.getUser().getKey(), new UserCallback() {
                    @Override
                    public void onSuccess(ArrayList<UserModel> users) {
                        if(users.size() > 0) {
                            UserModel user = users.get(0);
                            user.setState(isAccepted ? 1 : -1);
                            new UserController().save(user, new UserCallback() {
                                @Override
                                public void onSuccess(ArrayList<UserModel> users) {
                                    callback.onSuccess(requests);

                                }

                                @Override
                                public void onFail(String error) {
                                    callback.onFail(error);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFail(String error) {
                        callback.onFail(error);
                    }
                });
            }
            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

}
