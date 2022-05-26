package organizesports.controllers;

import androidx.annotation.NonNull;

import com.app.organizesports.callbacks.UserCallback;
import com.app.organizesports.callbacks.UserReqCallback;
import com.app.organizesports.models.UserModel;
import com.app.organizesports.models.UserReqModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class UserReqController {
    private String node = "RegistrationRequests";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<UserReqModel> requests = new ArrayList<>();

    public void save(final UserReqModel request, final UserReqCallback callback) {
        if(request.getKey() == null){
            request.setKey(myRef.push().getKey());
        }else if(request.getKey().equals("")){
            request.setKey(myRef.push().getKey());
        }

        myRef = database.getReference(node + "/" + request.getKey());
        myRef.setValue(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        requests.add(request);
                        callback.onSuccess(requests);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFail(e.toString());
                    }
                });
    }


    public void geRequestsAlways(final UserReqCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requests = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserReqModel request = snapshot.getValue(UserReqModel.class);
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




    public void newRequest(UserModel user, final UserReqCallback callback) {
        final UserReqModel request = new UserReqModel();
        request.setUser(user);
        request.setState(0);
        request.setCreatedAt(Calendar.getInstance().getTime());

        save(request, new UserReqCallback() {
            @Override
            public void onSuccess(ArrayList<UserReqModel> requests) {
                callback.onSuccess(requests);
            }
            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }

    public void responseOnRequest(UserReqModel request, Boolean isAccepted, final UserReqCallback callback) {
        request.setState(isAccepted ? 1 : -1);
        save(request, new UserReqCallback() {
            @Override
            public void onSuccess(ArrayList<UserReqModel> requests) {
                new UserController().getUserByKey(request.getUser().getKey(), new UserCallback() {
                    @Override
                    public void onSuccess(ArrayList<UserModel> stations) {
                        if(stations.size() > 0) {
                            UserModel user = stations.get(0);
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
