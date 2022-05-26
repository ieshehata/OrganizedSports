package organizesports.controllers;

import androidx.annotation.NonNull;

import com.app.organizesports.callbacks.DayCallback;
import com.app.organizesports.models.DayModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class DayController {
    private String node = "Days";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference(node);
    private ArrayList<DayModel> days = new ArrayList<>();

    public void save(final DayModel day, final DayCallback callback) {
        if(day.getKey() == null || day.getKey().equals("")){
            day.setKey(myRef.push().getKey());
        }

        myRef = database.getReference(node + "/" + day.getKey());
        myRef.setValue(day)
                .addOnSuccessListener(aVoid -> {
                    days.add(day);
                    callback.onSuccess(days);
                })
                .addOnFailureListener(e -> callback.onFail(e.toString()));
    }

    public void getDays(String userKey, final DayCallback callback){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                days = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DayModel day = snapshot.getValue(DayModel.class);
                    if(day.getUserKey().equals(userKey)) {
                        days.add(day);
                    }
                }
                callback.onSuccess(days);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getDaysAlways(String userKey, final DayCallback callback){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                days = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DayModel day = snapshot.getValue(DayModel.class);
                    if(day.getUserKey().equals(userKey)) {
                        days.add(day);
                    }
                }
                callback.onSuccess(days);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void getDayByKey(final String key, final DayCallback callback){
        Query query = myRef.orderByChild("key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                days = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DayModel model = snapshot.getValue(DayModel.class);
                    if(model.getKey().equals(key)) {
                        days.add(model);
                    }
                }
                callback.onSuccess(days);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onFail(databaseError.getMessage());
            }
        });
    }

    public void delete(DayModel day,final DayCallback callback) {
        myRef = database.getReference(node+"/"+day.getKey());
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
                        days = new ArrayList<>();
                        callback.onSuccess(days);
                    }
                });
    }

    public void newDay(String userKey, double weight, double height, double bmi, final DayCallback callback) {
        DayModel day = new DayModel();

        Calendar dayCalendar = Calendar.getInstance();
        dayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        dayCalendar.set(Calendar.MINUTE, 0);
        dayCalendar.set(Calendar.SECOND, 0);
        dayCalendar.set(Calendar.MILLISECOND, 0);
        day.setDay(dayCalendar.getTime());

        day.setUserKey(userKey);
        day.setWeight(weight);
        day.setHeight(height);
        day.setBmi(bmi);

        save(day, new DayCallback() {
            @Override
            public void onSuccess(ArrayList<DayModel> tasks) {
                callback.onSuccess(tasks);
            }
            @Override
            public void onFail(String error) {
                callback.onFail(error);
            }
        });
    }
}
