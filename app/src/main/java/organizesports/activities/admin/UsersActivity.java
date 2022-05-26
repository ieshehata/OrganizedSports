package organizesports.activities.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizesports.R;
import com.app.organizesports.adapters.UserAdapter;
import com.app.organizesports.callbacks.UserCallback;
import com.app.organizesports.controllers.UserController;
import com.app.organizesports.models.UserModel;
import com.app.organizesports.utils.LoadingHelper;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity implements UserAdapter.UserListener, View.OnClickListener {

    private LoadingHelper loadingHelper;
    private RecyclerView recyclerView;
    private Button allButton, activeButton, inactiveButton;
    private UserAdapter adapter;
    private ArrayList<UserModel> allCaregivers = new ArrayList<>();
    private ArrayList<UserModel> activeCaregivers = new ArrayList<>();
    private ArrayList<UserModel> inactiveCaregivers = new ArrayList<>();
    private int listFilter = 0; //0 -> All, 1 -> active, 2 -> inactive
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        recyclerView = findViewById(R.id.recycler_view);
        allButton = findViewById(R.id.all_button);
        activeButton = findViewById(R.id.active_button);
        inactiveButton = findViewById(R.id.inactive_button);

        allButton.setOnClickListener(UsersActivity.this);
        activeButton.setOnClickListener(UsersActivity.this);
        inactiveButton.setOnClickListener(UsersActivity.this);
        loadingHelper = new LoadingHelper(UsersActivity.this);

        load();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_button:
                listFilter = 0;
                allButton.setBackgroundColor(ContextCompat.getColor(UsersActivity.this, R.color.colorPrimaryDark));
                allButton.setTextColor(ContextCompat.getColor(UsersActivity.this, R.color.colorWhite));

                activeButton.setBackgroundColor(ContextCompat.getColor(UsersActivity.this, R.color.colorLightGray));
                activeButton.setTextColor(ContextCompat.getColor(UsersActivity.this, R.color.colorPrimaryDark));

                inactiveButton.setBackgroundColor(ContextCompat.getColor(UsersActivity.this, R.color.colorLightGray));
                inactiveButton.setTextColor(ContextCompat.getColor(UsersActivity.this, R.color.colorPrimaryDark));
                filterUpdated();
                break;

            case R.id.active_button:
                listFilter = 1;
                allButton.setBackgroundColor(ContextCompat.getColor(UsersActivity.this, R.color.colorLightGray));
                allButton.setTextColor(ContextCompat.getColor(UsersActivity.this, R.color.colorPrimaryDark));

                activeButton.setBackgroundColor(ContextCompat.getColor(UsersActivity.this, R.color.colorPrimaryDark));
                activeButton.setTextColor(ContextCompat.getColor(UsersActivity.this, R.color.colorWhite));

                inactiveButton.setBackgroundColor(ContextCompat.getColor(UsersActivity.this, R.color.colorLightGray));
                inactiveButton.setTextColor(ContextCompat.getColor(UsersActivity.this, R.color.colorPrimaryDark));
                filterUpdated();
                break;

            case R.id.inactive_button:
                listFilter = 2;
                allButton.setBackgroundColor(ContextCompat.getColor(UsersActivity.this, R.color.colorLightGray));
                allButton.setTextColor(ContextCompat.getColor(UsersActivity.this, R.color.colorPrimaryDark));

                activeButton.setBackgroundColor(ContextCompat.getColor(UsersActivity.this, R.color.colorLightGray));
                activeButton.setTextColor(ContextCompat.getColor(UsersActivity.this, R.color.colorPrimaryDark));

                inactiveButton.setBackgroundColor(ContextCompat.getColor(UsersActivity.this, R.color.colorPrimaryDark));
                inactiveButton.setTextColor(ContextCompat.getColor(UsersActivity.this, R.color.colorWhite));
                filterUpdated();
                break;
        }
    }

    private void load() {
        loadingHelper.showLoading("");
        new UserController().getUsersAlways(new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> users) {
                loadingHelper.dismissLoading();
                allCaregivers = new ArrayList<>();
                activeCaregivers = new ArrayList<>();
                inactiveCaregivers = new ArrayList<>();
                for(UserModel user : users) {
                    if(user.getState() == 1 && user.getUserType() == 2) {
                        allCaregivers.add(user);
                        activeCaregivers.add(user);
                    }else if(user.getState() == -1 && user.getUserType() == 2) {
                        allCaregivers.add(user);
                        inactiveCaregivers.add(user);
                    }
                }
                if(adapter == null || adapter.getData().size() == 0) {
                    if(listFilter == 0) {
                        adapter = new UserAdapter(allCaregivers, UsersActivity.this);
                    }else if(listFilter == 1) {
                        adapter = new UserAdapter(activeCaregivers, UsersActivity.this);
                    }else if(listFilter == 2) {
                        adapter = new UserAdapter(inactiveCaregivers, UsersActivity.this);
                    }

                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(UsersActivity.this));
                }else {
                    if(listFilter == 0) {
                        adapter.updateData(allCaregivers);
                    }else if(listFilter == 1) {
                        adapter.updateData(activeCaregivers);
                    }else if(listFilter == 2) {
                        adapter.updateData(inactiveCaregivers);
                    }
                }
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(UsersActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filterUpdated() {
        if(listFilter == 0) {
            adapter.updateData(allCaregivers);
        }else if(listFilter == 1) {
            adapter.updateData(activeCaregivers);
        }else if(listFilter == 2) {
            adapter.updateData(inactiveCaregivers);
        }
    }

    @Override
    public void response(int position, boolean isBlocking) {
        loadingHelper.showLoading("");
        UserModel user = adapter.getData().get(position);
        user.setState(isBlocking ? -1 : 1);
        new UserController().save(user, new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> stations)  {
                loadingHelper.dismissLoading();
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(UsersActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void view(int position) {

    }

    @Override
    public void deleteItem(int position) {
        loadingHelper.showLoading("");
        new UserController().delete(adapter.getData().get(position), new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> caregivers) {
                loadingHelper.dismissLoading();
                Toast.makeText(UsersActivity.this, "deleted!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(UsersActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}