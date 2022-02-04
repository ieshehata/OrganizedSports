package com.app.organizedsports.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.app.organizedsports.R;
import com.app.organizedsports.adapters.TipsAdapter;
import com.app.organizedsports.callback.TipCallback;
import com.app.organizedsports.controllers.TipController;
import com.app.organizedsports.dialogs.DataDialog;
import com.app.organizedsports.dialogs.TipDialog;
import com.app.organizedsports.model.TipsModel;
import com.app.organizedsports.utils.LoadingHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;

public class TipsActivity extends AppCompatActivity implements TipsAdapter.TipListener, DataDialog.DataDialogListener{
    RecyclerView list;
    TextView noList;
    private ArrayList<TipsModel> currentList;
    private LoadingHelper loadingHelper;
    private TipsAdapter adapter;
    private FloatingActionButton add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        list = findViewById(R.id.list);
        add = findViewById(R.id.add_button);
        noList = findViewById(R.id.no_items);
        loadingHelper = new LoadingHelper(this);

        // perform click event on button
        add.setOnClickListener(v -> {
            DataDialog dialog = new DataDialog();
            dialog.show(getSupportFragmentManager() , "data dialog");
        });


        loadingHelper.showLoading("");
        new TipController().getTips(new TipCallback() {
            @Override
            public void onSuccess(ArrayList<TipsModel> rates) {
                loadingHelper.dismissLoading();
                currentList = rates;
                noList.setText("No Tips Found!");
                if (currentList.size() > 0) {
                    list.setVisibility(View.VISIBLE);
                    noList.setVisibility(View.GONE);
                    if (adapter == null || adapter.getData().size() == 0) {
                        adapter = new TipsAdapter(currentList, (TipsAdapter.TipListener) TipsActivity.this);
                        list.setAdapter(adapter);
                        list.setLayoutManager(new LinearLayoutManager(TipsActivity.this));
                    } else {
                        adapter.updateData(currentList);
                    }
                } else {
                    noList.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFail(String msg) {
                loadingHelper.dismissLoading();
                Toast.makeText(TipsActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void getData( String tipText) {
        TipsModel model = new TipsModel();
        model.setCreatedAt(Calendar.getInstance().getTime());
        new TipController().newTip(tipText, new TipCallback() {
            @Override
            public void onSuccess(ArrayList<TipsModel> tips) {
                Toast.makeText(TipsActivity.this, "Send", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(TipsActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }


    @Override
    public void view(int position) {

    }

    @Override
    public void deleteItem(int position) {

    }
}