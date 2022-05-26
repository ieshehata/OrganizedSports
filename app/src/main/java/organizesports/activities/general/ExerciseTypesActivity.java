package organizesports.activities.general;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizesports.R;
import com.app.organizesports.activities.admin.ExerciseTypeEditorActivity;
import com.app.organizesports.adapters.ExerciseTypeAdapter;
import com.app.organizesports.callbacks.ExerciseTypeCallback;
import com.app.organizesports.controllers.ExerciseTypeController;
import com.app.organizesports.models.ExerciseTypeModel;
import com.app.organizesports.utils.LoadingHelper;
import com.app.organizesports.utils.SharedData;

import java.util.ArrayList;

public class ExerciseTypesActivity extends AppCompatActivity implements ExerciseTypeAdapter.ExerciseTypeListener {

    private ArrayList<ExerciseTypeModel> currentList = new ArrayList<>();
    private LoadingHelper loadingHelper;
    private ExerciseTypeAdapter adapter;
    private TextView noList;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_types);

        recyclerView = findViewById(R.id.recycler_view);
        noList = findViewById(R.id.no_list);
        loadingHelper = new LoadingHelper(this);

        Button add = findViewById(R.id.add);
        add.setOnClickListener(v -> startActivity(new Intent(this, ExerciseTypeEditorActivity.class)));
        add.setVisibility(SharedData.userType == 1 ? View.VISIBLE : View.GONE);

        getData();
    }


    private void getData() {
        loadingHelper.showLoading("");
        new ExerciseTypeController().getExerciseTypesAlways(new ExerciseTypeCallback() {
            @Override
            public void onSuccess(ArrayList<ExerciseTypeModel> exerciseTypes) {
                loadingHelper.dismissLoading();
                currentList = exerciseTypes;
                if (currentList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noList.setVisibility(View.GONE);
                    if (adapter == null || adapter.getData().size() == 0) {
                        adapter = new ExerciseTypeAdapter(currentList, ExerciseTypesActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new GridLayoutManager(ExerciseTypesActivity.this, 2));
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
                Toast.makeText(ExerciseTypesActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void view(int position) {
        SharedData.chosenExerciseType = adapter.getData().get(position);
        startActivity(new Intent(this, ExercisesActivity.class));
    }

    @Override
    public void deleteItem(int position) {
        new ExerciseTypeController().delete(adapter.getData().get(position), new ExerciseTypeCallback() {
            @Override
            public void onSuccess(ArrayList<ExerciseTypeModel> exerciseTypes) {
                Toast.makeText(ExerciseTypesActivity.this, "Deleted!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(ExerciseTypesActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}