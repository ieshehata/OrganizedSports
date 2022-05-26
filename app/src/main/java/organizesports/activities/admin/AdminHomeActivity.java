package organizesports.activities.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.app.organizesports.R;
import com.app.organizesports.activities.auth.UserTypeActivity;
import com.app.organizesports.activities.general.DataTypesActivity;
import com.app.organizesports.utils.SharedData;

public class AdminHomeActivity extends AppCompatActivity {

    LinearLayout data, users, chat, tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        data = findViewById(R.id.data);
        users = findViewById(R.id.users);
        chat = findViewById(R.id.chat);
        tips = findViewById(R.id.tips);

        data.setOnClickListener(v -> {
            startActivity(new Intent(this, DataTypesActivity.class));
        });

        users.setOnClickListener(v -> {
            startActivity(new Intent(this, UsersActivity.class));
        });

        chat.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminConversationsActivity.class));
        });

        tips.setOnClickListener(v -> {
            startActivity(new Intent(this, TipsActivity.class));
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_logout:
                SharedData.currentUser = null;
                Intent intent = new Intent(this, UserTypeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}