package organizesports.activities.general;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizesports.R;
import com.app.organizesports.adapters.ContentAdapter;
import com.app.organizesports.models.ElementModel;
import com.app.organizesports.utils.SharedData;

import java.util.ArrayList;

public class DataDetailsActivity extends AppCompatActivity {
    private ArrayList<ElementModel> currentList = new ArrayList<>();
    private ContentAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_details);
        recyclerView = findViewById(R.id.recycler_view);

        getData();
    }


    private void getData() {
        currentList = SharedData.currentElements;
        if (currentList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            if (adapter == null || adapter.getData().size() == 0) {
                adapter = new ContentAdapter(currentList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(DataDetailsActivity.this));
            } else {
                adapter.updateData(currentList);
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(this, "No Content!", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }
}