package organizesports.activities.admin;

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
import com.app.organizesports.adapters.TipAdapter;
import com.app.organizesports.callbacks.TipCallback;
import com.app.organizesports.controllers.TipController;
import com.app.organizesports.models.TipModel;
import com.app.organizesports.utils.LoadingHelper;
import com.app.organizesports.utils.SharedData;

import java.util.ArrayList;

public class TipsActivity extends AppCompatActivity {

    private LoadingHelper loadingHelper;
    private TipAdapter adapter;
    private TextView noList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);


        recyclerView = findViewById(R.id.recycler_view);
        noList = findViewById(R.id.no_list);
        loadingHelper = new LoadingHelper(this);

        Button add = findViewById(R.id.add);
        add.setOnClickListener(v -> {
            SharedData.currentTip = new TipModel();
            Intent intent = new Intent(this, TipEditorActivity.class);
            intent.putExtra("isNew", true);
            startActivity(intent);
        });
        add.setVisibility(SharedData.userType == 1 ? View.VISIBLE : View.GONE);

        getData();
    }

    private void getData() {
        new TipController().getTipsAlways(new TipCallback() {
            @Override
            public void onSuccess(ArrayList<TipModel> tips) {
                if(tips.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noList.setVisibility(View.GONE);
                    if(adapter == null || adapter.getData().size() == 0) {
                        adapter = new TipAdapter(tips);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(TipsActivity.this));
                    }else {
                        adapter.updateData(tips);
                    }
                }else {
                    noList.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(TipsActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}