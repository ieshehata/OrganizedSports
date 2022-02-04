package com.app.organizedsports.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.organizedsports.R;
import com.app.organizedsports.utils.SharedData;

public class UserTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);
        findViewById(R.id.admin).setOnClickListener(view -> {
            SharedData.userType = 1;
            Intent intent = new Intent(UserTypeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.user).setOnClickListener(view -> {
            SharedData.userType = 2;
            Intent intent = new Intent(UserTypeActivity.this,LoginActivity.class);
            startActivity(intent);
        });

    }
}