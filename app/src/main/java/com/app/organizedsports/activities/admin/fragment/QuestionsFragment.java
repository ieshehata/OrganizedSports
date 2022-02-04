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
import com.app.organizedsports.adapters.QuestionsAdapter;
import com.app.organizedsports.callback.QuestionsCallback;
import com.app.organizedsports.controllers.QuestionsController;
import com.app.organizedsports.dialogs.DataDialog;
import com.app.organizedsports.model.QuestionsModel;
import com.app.organizedsports.utils.LoadingHelper;
import com.app.organizedsports.utils.SharedData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class QuestionsFragment extends Fragment implements QuestionsAdapter.QuestionsListener , View.OnClickListener,DataDialog.DataDialogListener{
    private LinearLayout root;
    private LoadingHelper loadingHelper;
    private QuestionsAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noList;
    private ArrayList<QuestionsModel> currentList = new ArrayList<>();
    private QuestionsModel chosenQuestion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        root = view.findViewById(R.id.root);
        recyclerView = view.findViewById(R.id.recycler_view);
        noList = view.findViewById(R.id.no_items);
        loadingHelper = new LoadingHelper(getActivity());



        getData();

        return view;
    }


    private void getData() {
        loadingHelper.showLoading("");
        new QuestionsController().getQuestionsAlways(new QuestionsCallback() {
            @Override
            public void onSuccess(ArrayList<QuestionsModel> governorates) {
                loadingHelper.dismissLoading();
                currentList = governorates;
                SharedData.allQuestions = governorates;
                noList.setText("No Questions Found!");
                if (currentList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noList.setVisibility(View.GONE);
                    if (adapter == null || adapter.getData().size() == 0) {
                        adapter = new QuestionsAdapter(currentList, QuestionsFragment.this);
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
        chosenQuestion = adapter.getData().get(position);
        DataDialog dialog = new DataDialog(adapter.getData().get(position).getQuestion());
        dialog.show(QuestionsFragment.this.getChildFragmentManager(), "dialog");
    }

    @Override
    public void deleteItem(int position) {
        loadingHelper.showLoading("");
        new QuestionsController().delete(adapter.getData().get(position), new QuestionsCallback() {
            @Override
            public void onSuccess(ArrayList<QuestionsModel> governorates) {
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
    public void getData(String q) {
        chosenQuestion.setQuestion(q);
        new QuestionsController().save(chosenQuestion, new QuestionsCallback() {
            @Override
            public void onSuccess(ArrayList<QuestionsModel> governorates) { }

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
