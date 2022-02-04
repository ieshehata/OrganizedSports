package com.app.organizedsports.activities.admin.fragment;

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
import com.app.organizedsports.adapters.QuestionAndAnsAdapter;
import com.app.organizedsports.callback.QuestionAndAnsCallback;
import com.app.organizedsports.controllers.QuestionAndAnsController;
import com.app.organizedsports.dialogs.DataDialog;
import com.app.organizedsports.model.QuestionAndAnsModel;
import com.app.organizedsports.utils.LoadingHelper;
import com.app.organizedsports.utils.SharedData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class QuestionAndAnsFragment extends Fragment implements QuestionAndAnsAdapter.QuestionAndAnsListener , View.OnClickListener, DataDialog.DataDialogListener{
    private LinearLayout root;
    private LoadingHelper loadingHelper;
    private QuestionAndAnsAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noList;
    private ArrayList<QuestionAndAnsModel> currentList = new ArrayList<>();
    private QuestionAndAnsModel chosenQuestionAndAns;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions_and_ans, container, false);
        root = view.findViewById(R.id.root);
        recyclerView = view.findViewById(R.id.recycler_view);
        noList = view.findViewById(R.id.no_items);
        loadingHelper = new LoadingHelper(getActivity());
        FloatingActionButton add = view.findViewById(R.id.add_button);

       /* if (SharedData.userType == 1 ){
            add.setVisibility(View.VISIBLE);
        }else{
            add.setVisibility(View.GONE);
        }

        add.setOnClickListener(v -> {
          //  DataDialog dialog = new DataDialog();
           // dialog.show(getSupportFragmentManager() , "data dialog");
        });*/

        getData();

        return view;
    }

    private void getData() {
        loadingHelper.showLoading("");
        new QuestionAndAnsController().getQuestionAndAnsAlways(new QuestionAndAnsCallback() {
            @Override
            public void onSuccess(ArrayList<QuestionAndAnsModel> governorates) {
                loadingHelper.dismissLoading();
                currentList = governorates;
                SharedData.allQuestionAndAns = governorates;
                noList.setText("No Questions Found!");
                if (currentList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noList.setVisibility(View.GONE);
                    if (adapter == null || adapter.getData().size() == 0) {
                        adapter = new QuestionAndAnsAdapter(currentList, QuestionAndAnsFragment.this);
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
    public void onClick(int position) {
        chosenQuestionAndAns = adapter.getData().get(position);
        DataDialog dialog = new DataDialog(adapter.getData().get(position).getAns());
        dialog.show(QuestionAndAnsFragment.this.getChildFragmentManager(), "dialog");
    }

    @Override
    public void deleteItem(int position) {
        loadingHelper.showLoading("");
        new QuestionAndAnsController().delete(adapter.getData().get(position), new QuestionAndAnsCallback() {
            @Override
            public void onSuccess(ArrayList<QuestionAndAnsModel> governorates) {
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

    @Override
    public void getData(String ans) {
        chosenQuestionAndAns.setAns(ans);
        new QuestionAndAnsController().save(chosenQuestionAndAns, new QuestionAndAnsCallback() {
            @Override
            public void onSuccess(ArrayList<QuestionAndAnsModel> governorates) { }

            @Override
            public void onFail(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

    }
}
