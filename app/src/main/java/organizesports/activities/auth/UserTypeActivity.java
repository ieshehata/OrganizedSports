package organizesports.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.app.organizesports.R;
import com.app.organizesports.utils.SharedData;

public class UserTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        Button admin = findViewById(R.id.admin);
        Button user = findViewById(R.id.user);


        admin.setOnClickListener(v -> {
            SharedData.userType = 1;
            Intent intent = new Intent(UserTypeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        user.setOnClickListener(v -> {
            SharedData.userType = 2;
            Intent intent = new Intent(UserTypeActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}