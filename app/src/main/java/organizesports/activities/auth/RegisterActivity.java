package organizesports.activities.auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.organizesports.R;
import com.app.organizesports.activities.user.UserHomeActivity;
import com.app.organizesports.callbacks.StringCallback;
import com.app.organizesports.callbacks.UserCallback;
import com.app.organizesports.controllers.UploadController;
import com.app.organizesports.controllers.UserController;
import com.app.organizesports.models.UserModel;
import com.app.organizesports.utils.LoadingHelper;
import com.app.organizesports.utils.SharedData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.annotations.NotNull;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener{
    @NotNull
    @NotEmpty
    @Length(min = 3)
    TextInputEditText name;

    @NotNull
    @NotEmpty
    @Length(min = 8)
    TextInputEditText phone;

    @NotNull
    @NotEmpty
    @Length(min = 6)
    TextInputEditText password;

    @NotNull
    @NotEmpty
    @Email
    TextInputEditText email;

    private static final int PICK_IMAGE = 55;
    Uri imageUri;
    int gender = 1;  // 0->Female, 1->Male
    ImageView avatar;
    Button login,register;
    MaterialButton male, female;
    LoadingHelper loadingHelper;
    private Validator validator;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisiter);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Register");
        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        male = findViewById(R.id.male_button);
        female = findViewById(R.id.female_button);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        loadingHelper = new LoadingHelper(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        sharedPref = this.getSharedPreferences(SharedData.PREF_KEY, Context.MODE_PRIVATE);

        avatar.setOnClickListener(v -> {
            if(checkReadPermission()){
                pickImage();
            }
        });

        male.setOnClickListener(v -> {
            gender = 1;
            male.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
            male.setTextColor(ContextCompat.getColor(this, R.color.whiteCardColor));
            male.setStrokeWidth(0);

            female.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            female.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGray));
            female.setStrokeWidth(2);
        });

        female.setOnClickListener(v -> {
            gender = 0;
            female.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
            female.setTextColor(ContextCompat.getColor(this, R.color.whiteCardColor));
            female.setStrokeWidth(0);

            male.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            male.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGray));
            male.setStrokeWidth(2);
        });

        register.setOnClickListener(v -> {
            validator.validate();
        });

        login.setOnClickListener(v -> onBackPressed());

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
            avatar.setImageURI(imageUri);
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
                    UserModel user = new UserModel();
                    user.setName(name.getText().toString());
                    user.setPhone("+965" + phone.getText().toString());
                    user.setPass(password.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setGender(gender);
                    user.setProfileImage(text);

                    SharedData.currentUser = user;

                    new UserController().newUser(SharedData.currentUser, new UserCallback() {
                        @Override
                        public void onSuccess(ArrayList<UserModel> users) {
                            if(users.size() > 0) {
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean(SharedData.IS_USER_SAVED, true);
                                editor.putString(SharedData.PHONE, users.get(0).getPhone());
                                editor.putString(SharedData.PASS, users.get(0).getPass());
                                editor.apply();

                                loadingHelper.dismissLoading();
                                Intent intent = new Intent(RegisterActivity.this, UserHomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        }

                        @Override
                        public void onFail(String error) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onFail(String error) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(RegisterActivity.this, "Pick your profile picture!", Toast.LENGTH_SHORT).show();
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