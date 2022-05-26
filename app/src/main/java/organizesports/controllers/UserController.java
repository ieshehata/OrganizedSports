package organizesports.controllers;


import androidx.annotation.NonNull;

import com.app.organizesports.callbacks.BooleanCallback;
import com.app.organizesports.callbacks.UserCallback;
import com.app.organizesports.models.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserController {
    private String node = "Users";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<UserModel> users = new ArrayList<>();

    public void save(final UserModel model, final UserCallback callback) {
        if(model.getKey() == null || model.getKey().equals("")){
            model.setKey(myRef.push().getKey());
        }

        myRef = database.getReference(node + "/" + model.getKey());
        myRef.setValue(model)
                .addOnSuccessListener(aVoid -> {
                    users.add(model);
                    callback.onSuccess(users);
                })
                .addOnFailureListener(e -> callback.onFail(e.toString()));
    }


    public void getUsers(final UserCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserModel model = snapshot.getValue(UserModel.class);
                    users.add(model);
                }
                callback.onSuccess(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getUsersAlways(final UserCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserModel model = snapshot.getValue(UserModel.class);
                    users.add(model);
                }
                callback.onSuccess(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getUserByKey(final String key, final UserCallback callback){
        Query query = myRef.orderByChild("key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserModel model = snapshot.getValue(UserModel.class);
                    if(model.getKey().equals(key)) {
                        users.add(model);
                    }
                }
                callback.onSuccess(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getUserByKeyAlways(final String key, final UserCallback callback){
        Query query = myRef.orderByChild("key").equalTo(key);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserModel model = snapshot.getValue(UserModel.class);
                    if(model.getKey().equals(key)) {
                        users.add(model);
                    }
                }
                callback.onSuccess(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getUserByPhone(final String phone, final UserCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserModel user1 = snapshot.getValue(UserModel.class);
                    assert user1 != null;
                    if(user1.getPhone().equals(phone)) {
                        users.add(user1);
                    }
                }
                callback.onSuccess(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void checkLogin(final UserModel user, final UserCallback callback){
        Query query = myRef.orderByChild("phone").equalTo(user.getPhone());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserModel model = snapshot.getValue(UserModel.class);
                    assert model != null;
                    if(model.getPhone().equals(user.getPhone()) && model.getPass().equals(user.getPass()) && model.getUserType() == user.getUserType())
                        users.add(model);
                }
                callback.onSuccess(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void checkEmail(final String email, final BooleanCallback callback){
        Query query = myRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean check = true;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserModel model = snapshot.getValue(UserModel.class);
                    assert model != null;
                    if(model.getEmail().equals(email))
                        check = false;
                }
                callback.onSuccess(check);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void checkPhone(final String phone, final BooleanCallback callback){
        Query query = myRef.orderByChild("phone").equalTo(phone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean check = true;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserModel user1 = snapshot.getValue(UserModel.class);
                    assert user1 != null;
                    if(user1.getPhone().equals(phone))
                        check = false;

                }
                callback.onSuccess(check);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void delete(UserModel station,final UserCallback callback) {
        myRef = database.getReference(node+"/"+station.getKey());
        myRef.removeValue()
                .addOnFailureListener(e -> callback.onFail(e.toString()))
                .addOnSuccessListener(aVoid -> {
                    users = new ArrayList<>();
                    callback.onSuccess(users);
                });
    }

    public void newUser(UserModel model, final UserCallback userCallback) {
        model.setPublic(true);
        model.setActivated(1);
        model.setState(1);
        model.setUserType(2);
        if(model.validate()) {
            checkEmail(model.getEmail(), new BooleanCallback() {
                @Override
                public void onSuccess(boolean bool) {
                    if(bool) {
                        checkPhone(model.getPhone(), new BooleanCallback() {
                            @Override
                            public void onSuccess(boolean bool) {
                                if(bool) {
                                    save(model, new UserCallback() {
                                        @Override
                                        public void onSuccess(ArrayList<UserModel> stations) {
                                            userCallback.onSuccess(stations);
                                        }
                                        @Override
                                        public void onFail(String error) {
                                            userCallback.onFail(error);
                                        }
                                    });
                                }else {
                                    userCallback.onFail("Phone is used before!");
                                }
                            }

                            @Override
                            public void onFail(String error) {
                                userCallback.onFail(error);
                            }

                        });
                    }else {
                        userCallback.onFail("Email is used before!");
                    }
                }
                @Override
                public void onFail(String error) {
                    userCallback.onFail(error);
                }
            });
        } else {
            userCallback.onFail("Not Valid Data!");
        }
    }


}
