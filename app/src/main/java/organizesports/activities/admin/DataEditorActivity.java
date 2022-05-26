package organizesports.activities.admin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.organizesports.R;
import com.app.organizesports.activities.general.DataDetailsActivity;
import com.app.organizesports.adapters.ElementAdapter;
import com.app.organizesports.callbacks.DataCallback;
import com.app.organizesports.callbacks.StringCallback;
import com.app.organizesports.controllers.DataController;
import com.app.organizesports.controllers.UploadController;
import com.app.organizesports.models.DataModel;
import com.app.organizesports.models.ElementModel;
import com.app.organizesports.utils.LoadingHelper;
import com.app.organizesports.utils.SharedData;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.annotations.NotNull;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DataEditorActivity extends AppCompatActivity implements Validator.ValidationListener, ElementAdapter.ElementListener {

    @NotNull
    @NotEmpty
    @Length(min = 3)
    TextInputEditText title;

    @NotNull
    @NotEmpty
    @Length(min = 3)
    TextInputEditText description;

    private static final int PICK_IMAGE = 55;
    private Uri imageUri;
    private ImageView image;
    private RecyclerView recyclerView;
    private TextView noList;
    private Button save, addElement, preview;
    private LoadingHelper loadingHelper;
    private Validator validator;

    private ElementAdapter adapter;
    private DataModel dataModel = new DataModel();
    private ArrayList<ElementModel> contentElements = new ArrayList<>();
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_editor);
        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        recyclerView = findViewById(R.id.recycler_view);
        noList = findViewById(R.id.no_list);
        save = findViewById(R.id.save);
        addElement = findViewById(R.id.add_element);
        preview = findViewById(R.id.preview);

        loadingHelper = new LoadingHelper(this);
        validator = new Validator(this);
        validator.setValidationListener(this);

        image.setOnClickListener(v -> {
            if(checkReadPermission()){
                pickImage();
            }
        });

        save.setOnClickListener(v -> {
            validator.validate();
        });

        addElement.setOnClickListener(v -> {
            SharedData.currentElements = contentElements;
            startActivity(new Intent(this, DataElementEditorActivity.class));
        });

        preview.setOnClickListener(v -> {
            SharedData.currentElements = contentElements;
            startActivity(new Intent(this, DataDetailsActivity.class));
        });
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        contentElements = SharedData.currentElements;
        refreshAdapter();
    }

    private void getData() {
        if(SharedData.chosenData != null) {
            isEditing = true;
            dataModel = SharedData.chosenData;
            if (!TextUtils.isEmpty(dataModel.getIcon())) {
                image.setImageTintList(null);
                Picasso.get()
                        .load(dataModel.getIcon())
                        .into(image);
            }
            title.setText(dataModel.getTitle());
            description.setText(dataModel.getDescription());
            contentElements = dataModel.getElements();
            SharedData.currentElements = contentElements;
        } else {
            isEditing = false;
            SharedData.currentElements = contentElements = new ArrayList<>();
        }
    }

    private void refreshAdapter() {
        if (contentElements.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            noList.setVisibility(View.GONE);
            if (adapter == null || adapter.getData().size() == 0) {
                adapter = new ElementAdapter(contentElements, DataEditorActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(DataEditorActivity.this));
            } else {
                adapter.updateData(contentElements);
            }
        } else {
            noList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private boolean checkReadPermission(){
        int permissionWriteExternal = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            return false;
        }else{
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 2){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                pickImage();
            }
        }
    }

    private void pickImage(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }


    @Override
    public void onValidationSucceeded() {
        if(imageUri != null) {
            loadingHelper.showLoading("");
            new UploadController().uploadImage(imageUri, new StringCallback() {
                @Override
                public void onSuccess(String text) {
                    loadingHelper.dismissLoading();
                    dataModel.setTitle(title.getText().toString());
                    dataModel.setDescription(description.getText().toString());
                    dataModel.setElements(contentElements);
                    if(SharedData.chosenExerciseType != null)
                        dataModel.setExerciseKey(SharedData.chosenExerciseType.getKey());
                    dataModel.setType(SharedData.currentDataType);
                    dataModel.setIcon(text);

                    new DataController().save(dataModel, new DataCallback() {
                        @Override
                        public void onSuccess(ArrayList<DataModel> exerciseTypes) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(DataEditorActivity.this, "Saved Successfully!", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }

                        @Override
                        public void onFail(String error) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(DataEditorActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onFail(String error) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(DataEditorActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }else if(isEditing){
            loadingHelper.showLoading("");
            dataModel.setTitle(title.getText().toString());
            dataModel.setDescription(description.getText().toString());
            dataModel.setElements(contentElements);
            if(SharedData.chosenExerciseType != null)
                dataModel.setExerciseKey(SharedData.chosenExerciseType.getKey());
            dataModel.setType(SharedData.currentDataType);
            new DataController().save(dataModel, new DataCallback() {
                @Override
                public void onSuccess(ArrayList<DataModel> exerciseTypes) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(DataEditorActivity.this, "Saved Successfully!", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }

                @Override
                public void onFail(String error) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(DataEditorActivity.this, error, Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(DataEditorActivity.this, "Enter Image first!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for(ValidationError error: errors){
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if( view instanceof TextInputEditText){
                ((TextInputEditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void view(int position) {
        // Nothing to do here!
    }

    @Override
    public void deleteItem(int position) {
        contentElements.remove(position);
        SharedData.currentElements = contentElements;
        refreshAdapter();
    }

    @Override
    public void editItem(int position) {
        SharedData.currentElements = contentElements;
        SharedData.chosenElement = contentElements.get(position);
        SharedData.chosenElementIndex = position;
        startActivity(new Intent(this, DataElementEditorActivity.class));
    }
}