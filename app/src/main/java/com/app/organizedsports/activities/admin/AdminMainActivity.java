package com.app.organizedsports.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.app.organizedsports.R;
import com.app.organizedsports.activities.auth.UserTypeActivity;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        (findViewById(R.id.q_a)).setOnClickListener(v -> startActivity(new Intent(this, QuestionsActivity.class)));

        (findViewById(R.id.tips)).setOnClickListener(v -> startActivity(new Intent(this, TipsActivity.class)));

        (findViewById(R.id.exercises)).setOnClickListener(v -> startActivity(new Intent(this, ExercisesActivity.class)));

        (findViewById(R.id.users)).setOnClickListener(v -> startActivity(new Intent(this, UsersActivity.class)));

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_logout:
                Intent intent = new Intent(this, UserTypeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}