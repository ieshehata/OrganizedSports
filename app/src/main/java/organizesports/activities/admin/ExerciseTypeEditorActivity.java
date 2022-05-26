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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.organizesports.R;
import com.app.organizesports.callbacks.ExerciseTypeCallback;
import com.app.organizesports.callbacks.StringCallback;
import com.app.organizesports.controllers.ExerciseTypeController;
import com.app.organizesports.controllers.UploadController;
import com.app.organizesports.models.ExerciseTypeModel;
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

public class ExerciseTypeEditorActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotNull
    @NotEmpty
    @Length(min = 3)
    TextInputEditText title;


    private static final int PICK_IMAGE = 55;
    Uri imageUri;
    ImageView icon;
    Button save;
    LoadingHelper loadingHelper;
    private Validator validator;

    ExerciseTypeModel exerciseType = new ExerciseTypeModel();

    boolean isEditing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_type_editor);

        icon = findViewById(R.id.icon);
        title = findViewById(R.id.title);
        save = findViewById(R.id.save);

        loadingHelper = new LoadingHelper(this);
        validator = new Validator(this);
        validator.setValidationListener(this);

        icon.setOnClickListener(v -> {
            if(checkReadPermission()){
                pickImage();
            }
        });

        save.setOnClickListener(v -> {
            validator.validate();
        });

        getData();
    }

    private void getData() {
        if(SharedData.chosenExerciseType != null) {
            isEditing = true;
            exerciseType = SharedData.chosenExerciseType;
            if (!TextUtils.isEmpty(exerciseType.getIcon())) {
                icon.setImageTintList(null);
                Picasso.get()
                        .load(exerciseType.getIcon())
                        .into(icon);
            }
            title.setText(exerciseType.getTitle());
        } else {
            isEditing = false;
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
            icon.setImageURI(imageUri);
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
                    exerciseType.setTitle(title.getText().toString());
                    exerciseType.setIcon(text);

                    new ExerciseTypeController().save(exerciseType, new ExerciseTypeCallback() {
                        @Override
                        public void onSuccess(ArrayList<ExerciseTypeModel> exerciseTypes) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(ExerciseTypeEditorActivity.this, "Saved Successfully!", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }

                        @Override
                        public void onFail(String error) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(ExerciseTypeEditorActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onFail(String error) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(ExerciseTypeEditorActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }else if(isEditing){
            loadingHelper.showLoading("");
            exerciseType.setTitle(title.getText().toString());

            new ExerciseTypeController().save(exerciseType, new ExerciseTypeCallback() {
                @Override
                public void onSuccess(ArrayList<ExerciseTypeModel> exerciseTypes) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(ExerciseTypeEditorActivity.this, "Saved Successfully!", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }

                @Override
                public void onFail(String error) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(ExerciseTypeEditorActivity.this, error, Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(ExerciseTypeEditorActivity.this, "Enter Icon first!", Toast.LENGTH_LONG).show();

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
}