package com.app.organizedsports.activities.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizedsports.R;
import com.app.organizedsports.activities.user.UsersDetailsActivity;
import com.app.organizedsports.adapters.UserAdapter;
import com.app.organizedsports.callback.UserCallback;
import com.app.organizedsports.controllers.UserController;
import com.app.organizedsports.model.UserModel;
import com.app.organizedsports.utils.LoadingHelper;
import com.app.organizedsports.utils.SharedData;

import java.util.ArrayList;

public class UsersFragment extends Fragment implements UserAdapter.UserListener , View.OnClickListener{
    private LoadingHelper loadingHelper;
    private RecyclerView recyclerView;
    private Button allButton, activeButton, inactiveButton;
    private UserAdapter adapter;
    private ArrayList<UserModel> allUsers = new ArrayList<>();
    private ArrayList<UserModel> activeUsers = new ArrayList<>();
    private ArrayList<UserModel> inactiveUsers = new ArrayList<>();
    private int listFilter = 0; //0 -> All, 1 -> active, 2 -> inactive

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        allButton = view.findViewById(R.id.all_button);
        activeButton = view.findViewById(R.id.active_button);
        inactiveButton = view.findViewById(R.id.inactive_button);
        allButton.setOnClickListener( UsersFragment.this);
        activeButton.setOnClickListener(UsersFragment.this);
        inactiveButton.setOnClickListener( UsersFragment.this);
        loadingHelper = new LoadingHelper(getActivity());
        load();
        return view;
    }

    private void load() {
        loadingHelper.showLoading("");
        new UserController().getUsersAlways(new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> users) {
                loadingHelper.dismissLoading();
                allUsers = new ArrayList<>();
                activeUsers = new ArrayList<>();
                inactiveUsers = new ArrayList<>();
                for(UserModel user : users) {
                    if(user.getState() == 1 && user.getUserType() == 2) {
                        allUsers.add(user);
                        activeUsers.add(user);
                    }else if(user.getState() == -1&& user.getUserType() == 2 ) {
                        allUsers.add(user);
                        inactiveUsers.add(user);
                    }
                }
                if(adapter == null || adapter.getData().size() == 0) {
                    if(listFilter == 0) {
                        adapter = new UserAdapter(allUsers, UsersFragment.this);
                    }else if(listFilter == 1) {
                        adapter = new UserAdapter(activeUsers, UsersFragment.this);
                    }else if(listFilter == 2) {
                        adapter = new UserAdapter(inactiveUsers, UsersFragment.this);
                    }
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }else {
                    if(listFilter == 0) {
                        adapter.updateData(allUsers);
                    }else if(listFilter == 1) {
                        adapter.updateData(activeUsers);
                    }else if(listFilter == 2) {
                        adapter.updateData(inactiveUsers);
                    }
                }
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_button:
                listFilter = 0;
                allButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryMidDark));
                allButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

                activeButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
                activeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVeryDark));

                inactiveButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
                inactiveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVeryDark));
                filterUpdated();
                break;

            case R.id.active_button:
                listFilter = 1;
                allButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
                allButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVeryDark));

                activeButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryMidDark));
                activeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

                inactiveButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
                inactiveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVeryDark));
                filterUpdated();
                break;

            case R.id.inactive_button:
                listFilter = 2;
                allButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
                allButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVeryDark));

                activeButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLightGray));
                activeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryVeryDark));

                inactiveButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryMidDark));
                inactiveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                filterUpdated();
                break;
        }
    }

    private void filterUpdated() {
        if(listFilter == 0) {
            adapter.updateData(allUsers);
        }else if(listFilter == 1) {
            adapter.updateData(activeUsers);
        }else if(listFilter == 2) {
            adapter.updateData(inactiveUsers);
        }
    }

    @Override
    public void response(int position, boolean isBlocking) {
        loadingHelper.showLoading("");
        UserModel user = adapter.getData().get(position);
        user.setState(isBlocking ? -1 : 1);
        user.setActivated(isBlocking ? -1 : 1);
        new UserController().save(user, new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> stations)  {
                loadingHelper.dismissLoading();
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void view(int position) {
        SharedData.user = adapter.getData().get(position);
        Intent intent = new Intent(getActivity(), UsersDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void deleteItem(int position) {
        loadingHelper.showLoading("");
        new UserController().delete(adapter.getData().get(position), new UserCallback() {
            @Override
            public void onSuccess(ArrayList<UserModel> caregivers) {
                loadingHelper.dismissLoading();
                Toast.makeText(getActivity(), "deleted!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
