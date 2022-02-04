package com.app.organizedsports.activities.admin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizedsports.R;
import com.app.organizedsports.activities.user.UsersDetailsActivity;
import com.app.organizedsports.adapters.UserReqAdapter;
import com.app.organizedsports.callback.UserReqCallback;
import com.app.organizedsports.controllers.UserReqController;
import com.app.organizedsports.model.UsersReqModel;
import com.app.organizedsports.utils.LoadingHelper;
import com.app.organizedsports.utils.SharedData;

import java.util.ArrayList;

public class UserReqFragment extends Fragment implements UserReqAdapter.UserReqListener{
    private LinearLayout root;
    private LoadingHelper loadingHelper;
    private UserReqAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noList;
    private ArrayList<UsersReqModel> currentList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        root = view.findViewById(R.id.root);
        recyclerView = view.findViewById(R.id.recycler_view);
        noList = view.findViewById(R.id.no_items);
        loadingHelper = new LoadingHelper(getActivity());
        root.setBackgroundColor(getActivity().getColor(R.color.colorWhite));


        getData();
        return view;
    }


    private void getData() {
        loadingHelper.showLoading("");
        new UserReqController().geRequestsAlways(new UserReqCallback() {
            @Override
            public void onSuccess(ArrayList<UsersReqModel> requests) {
                loadingHelper.dismissLoading();
                currentList = requests;
                noList.setText("No Requests Found!");
                if (currentList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noList.setVisibility(View.GONE);
                    if (adapter == null || adapter.getData().size() == 0) {
                        adapter = new UserReqAdapter(currentList, UserReqFragment.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    } else {
                        adapter.updateData(currentList);
                    }
                } else {
                    noList.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
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
    public void response(int position, boolean isAccepted) {
        loadingHelper.showLoading("");
        new UserReqController().responseOnRequest(adapter.getData().get(position), isAccepted, new UserReqCallback() {
            @Override
            public void onSuccess(ArrayList<UsersReqModel> requests) {
                loadingHelper.dismissLoading();
                Toast.makeText(getActivity(), "Done!", Toast.LENGTH_LONG).show();
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
        SharedData.user = adapter.getData().get(position).getUser();
        Intent intent = new Intent(getActivity(), UsersDetailsActivity.class);
        startActivity(intent);
    }
}
