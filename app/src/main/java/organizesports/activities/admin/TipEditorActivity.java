package organizesports.activities.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.organizesports.R;
import com.app.organizesports.callbacks.TipCallback;
import com.app.organizesports.controllers.TipController;
import com.app.organizesports.models.TipModel;
import com.app.organizesports.utils.LoadingHelper;
import com.app.organizesports.utils.SharedData;

import java.util.ArrayList;

public class TipEditorActivity extends AppCompatActivity {
    LinearLayout inLayout, outLayout;
    EditText titleET, textET;
    TextView title, text;
    Button save;
    LoadingHelper loadingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_editor);
        boolean isNew = getIntent().getBooleanExtra("isNew", false);
        inLayout = findViewById(R.id.in);
        outLayout = findViewById(R.id.out);
        titleET = findViewById(R.id.title_et);
        textET = findViewById(R.id.text_et);
        title = findViewById(R.id.title);
        text = findViewById(R.id.text);
        save = findViewById(R.id.save);
        loadingHelper = new LoadingHelper(this);


        title.setText(SharedData.currentTip.getTitle());
        text.setText(SharedData.currentTip.getText());
        titleET.setText(SharedData.currentTip.getTitle());
        textET.setText(SharedData.currentTip.getText());

        if(SharedData.userType == 1) {
            inLayout.setVisibility(View.VISIBLE);
            outLayout.setVisibility(View.GONE);
        }else {
            inLayout.setVisibility(View.GONE);
            outLayout.setVisibility(View.VISIBLE);
        }


        save.setOnClickListener(v -> {
            if(!titleET.getText().toString().trim().isEmpty() && !textET.getText().toString().trim().isEmpty()) {
                loadingHelper.showLoading("");
                if(isNew) {
                    new TipController().newTip(titleET.getText().toString(), textET.getText().toString(),  new TipCallback() {
                        @Override
                        public void onSuccess(ArrayList<TipModel> tips) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(TipEditorActivity.this, "Added", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }

                        @Override
                        public void onFail(String error) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(TipEditorActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
                }else {
                    SharedData.currentTip.setTitle(titleET.getText().toString());
                    SharedData.currentTip.setText(textET.getText().toString());
                    new TipController().save(SharedData.currentTip, new TipCallback() {
                        @Override
                        public void onSuccess(ArrayList<TipModel> tips) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(TipEditorActivity.this, "Saved", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }

                        @Override
                        public void onFail(String error) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(TipEditorActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }else {
                Toast.makeText(this, "Check the data!", Toast.LENGTH_LONG).show();
            }
        });
    }
}