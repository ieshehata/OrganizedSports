package organizesports.activities.general;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizesports.R;
import com.app.organizesports.activities.admin.DataEditorActivity;
import com.app.organizesports.adapters.DataAdapter;
import com.app.organizesports.callbacks.DataCallback;
import com.app.organizesports.controllers.DataController;
import com.app.organizesports.models.DataModel;
import com.app.organizesports.utils.LoadingHelper;
import com.app.organizesports.utils.SharedData;

import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity implements DataAdapter.DataListener {

    private ArrayList<DataModel> currentList = new ArrayList<>();
    private LoadingHelper loadingHelper;
    private DataAdapter adapter;
    private TextView noList;
    private RecyclerView recyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        recyclerView = findViewById(R.id.recycler_view);
        noList = findViewById(R.id.no_list);
        loadingHelper = new LoadingHelper(this);

        Button add = findViewById(R.id.add);
        add.setOnClickListener(v -> {
            SharedData.chosenData = null;
            startActivity(new Intent(this, DataEditorActivity.class));
        });
        add.setVisibility(SharedData.userType == 1 ? View.VISIBLE : View.GONE);

        getData();
    }

    private void getData() {
        loadingHelper.showLoading("");
        new DataController().getRecipesAlways(new DataCallback() {
            @Override
            public void onSuccess(ArrayList<DataModel> exerciseTypes) {
                loadingHelper.dismissLoading();
                currentList = exerciseTypes;
                if (currentList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noList.setVisibility(View.GONE);
                    if (adapter == null || adapter.getData().size() == 0) {
                        adapter = new DataAdapter(currentList, RecipesActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(RecipesActivity.this));
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
                Toast.makeText(RecipesActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void view(int position) {
        SharedData.chosenData = adapter.getData().get(position);
        SharedData.currentElements = adapter.getData().get(position).getElements();
        startActivity(new Intent(this, DataDetailsActivity.class));
    }

    @Override
    public void editItem(int position) {
        SharedData.chosenData = adapter.getData().get(position);
        startActivity(new Intent(this, DataEditorActivity.class));
    }

    @Override
    public void deleteItem(int position) {
        new DataController().delete(adapter.getData().get(position), new DataCallback() {
            @Override
            public void onSuccess(ArrayList<DataModel> exerciseTypes) {
                Toast.makeText(RecipesActivity.this, "Deleted!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(RecipesActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}