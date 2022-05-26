package organizesports.activities.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.organizesports.R;
import com.app.organizesports.activities.admin.TipsActivity;
import com.app.organizesports.activities.auth.UserTypeActivity;
import com.app.organizesports.activities.general.ChatActivity;
import com.app.organizesports.activities.general.ExerciseTypesActivity;
import com.app.organizesports.activities.general.RecipesActivity;
import com.app.organizesports.activities.general.SupplementsActivity;
import com.app.organizesports.callbacks.ConversationCallback;
import com.app.organizesports.callbacks.DayCallback;
import com.app.organizesports.controllers.ConversationController;
import com.app.organizesports.controllers.DayController;
import com.app.organizesports.models.ConversationModel;
import com.app.organizesports.models.DayModel;
import com.app.organizesports.utils.LoadingHelper;
import com.app.organizesports.utils.SharedData;
import com.app.organizesports.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;

public class UserHomeActivity extends AppCompatActivity {

    private LinearLayout exercises, supplements, recipes, charts, chat, tips;
    private LoadingHelper loadingHelper;
    private Button progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        progress = findViewById(R.id.progress);

        exercises = findViewById(R.id.exercises);
        supplements = findViewById(R.id.supplements);
        recipes = findViewById(R.id.recipes);
        charts = findViewById(R.id.charts);
        chat = findViewById(R.id.chat);
        tips = findViewById(R.id.tips);
        loadingHelper = new LoadingHelper(this);

        new DayController().getDaysAlways(SharedData.currentUser.getKey(), new DayCallback() {
            @Override
            public void onSuccess(ArrayList<DayModel> days) {
                SharedData.currentUser.setDays(days);
            }
            @Override
            public void onFail(String error) {}
        });

        progress.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProgressActivity.class);
            for(DayModel dayModel : SharedData.currentUser.getDays()) {
                if(Utils.isSameDay(dayModel.getDay(), Calendar.getInstance().getTime())) {
                    intent.putExtra("isEditing", true);
                    SharedData.currentDay = dayModel;
                }
            }
            startActivity(intent);
        });

        exercises.setOnClickListener(v -> {
            SharedData.currentDataType = 1;
            startActivity(new Intent(this, ExerciseTypesActivity.class));
        });

        supplements.setOnClickListener(v -> {
            SharedData.currentDataType = 2;
            startActivity(new Intent(this, SupplementsActivity.class));
        });

        recipes.setOnClickListener(v -> {
            SharedData.currentDataType = 3;
            startActivity(new Intent(this, RecipesActivity.class));
        });

        charts.setOnClickListener(v -> {
            SharedData.currentDataType = 4;
            startActivity(new Intent(this, ChartsActivity.class));
        });

        chat.setOnClickListener(v -> {
            loadingHelper.showLoading("");
            new ConversationController().getConversationsByTwoUsers(SharedData.currentUser.getKey(),
                    SharedData.adminUser.getKey(), new ConversationCallback() {
                        @Override
                        public void onSuccess(ArrayList<ConversationModel> conversations) {
                            if (conversations.size() > 0) {
                                loadingHelper.dismissLoading();
                                SharedData.currentConversation = conversations.get(0);
                                Intent intent = new Intent(UserHomeActivity.this, ChatActivity.class);
                                startActivity(intent);
                            } else {
                                new ConversationController().newConversation(SharedData.adminUser, new ConversationCallback() {
                                    @Override
                                    public void onSuccess(ArrayList<ConversationModel> conversations) {
                                        loadingHelper.dismissLoading();
                                        SharedData.currentConversation = conversations.get(0);
                                        Intent intent = new Intent(UserHomeActivity.this, ChatActivity.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onFail(String error) {
                                        loadingHelper.dismissLoading();
                                        Toast.makeText(UserHomeActivity.this, error, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFail(String error) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(UserHomeActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        tips.setOnClickListener(v -> {
            startActivity(new Intent(UserHomeActivity.this, TipsActivity.class));
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_logout:
                SharedData.currentUser = null;
                SharedPreferences sharedPref = this.getSharedPreferences(SharedData.PREF_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(SharedData.IS_USER_SAVED, false);
                editor.apply();
                Intent intent = new Intent(this, UserTypeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;

            case R.id.action_profile:
                SharedData.stalkedUserHeader = SharedData.currentUser.toHeader();
                Intent profile = new Intent(this, ProfileEditorActivity.class);
                startActivity(profile);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}