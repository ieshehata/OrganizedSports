package com.app.organizedsports.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.organizedsports.R;
import com.app.organizedsports.callback.StringCallback;
import com.app.organizedsports.callback.UserCallback;
import com.app.organizedsports.controllers.UploadController;
import com.app.organizedsports.controllers.UserController;
import com.app.organizedsports.model.UserModel;
import com.app.organizedsports.utils.LoadingHelper;
import com.app.organizedsports.utils.SharedData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.annotations.NotNull;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener {

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
    @Email
    TextInputEditText email;

    @NotNull
    @NotEmpty
    @Length(min = 6)
    TextInputEditText password;


    private static final int PICK_IMAGE = 55;
    private boolean isEditing = false;
    private UserModel user = new UserModel();
    private LinearLayout governorateLayout, cityLayout;
    private AutoCompleteTextView governorate, city;
    Uri imageUri;
    int gender = 1;  // 0->Female, 1->Male
    SimpleDateFormat sdf = new SimpleDateFormat(SharedData.formatDate, Locale.US);
    ImageView avatar;
    Button register;
    MaterialButton male, female;
    LoadingHelper loadingHelper;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        isEditing = getIntent().getBooleanExtra("isEditing", false);
        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        male = findViewById(R.id.male_button);
        female = findViewById(R.id.female_button);
        register = findViewById(R.id.register);

        loadingHelper = new LoadingHelper(this);
        validator = new Validator(this);
        validator.setValidationListener(this);

        register.setText(SharedData.userType == 2 ? "Apply" : "Register");

        avatar.setOnClickListener(v -> {
            if (checkReadPermission()) {
                pickImage();
            }
        });

        male.setOnClickListener(v -> {
            gender = 1;
            male.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimaryMidDark)));
            male.setTextColor(ContextCompat.getColor(this, R.color.whiteCardColor));
            male.setStrokeWidth(0);

            female.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            female.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGray));
            female.setStrokeWidth(2);
        });

        female.setOnClickListener(v -> {
            gender = 0;
            female.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimaryMidDark)));
            female.setTextColor(ContextCompat.getColor(this, R.color.whiteCardColor));
            female.setStrokeWidth(0);

            male.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            male.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGray));
            male.setStrokeWidth(2);
        });


        register.setOnClickListener(v -> {
            validator.validate();
        });

        setData();

    }


    @SuppressLint("DefaultLocale")
    private void setData() {
        if (isEditing) {
            user = SharedData.user;

            Objects.requireNonNull(getSupportActionBar()).setTitle("Supplier Profile");
            register.setText("Update");
            name.setText(SharedData.user.getName());
            phone.setText(SharedData.user.getPhone());
            email.setText(SharedData.user.getEmail());
            password.setText(SharedData.user.getPassword());

            if (!TextUtils.isEmpty(SharedData.user.getImage())) {
                Picasso.get()
                        .load(SharedData.user.getImage())
                        .into(avatar);
            }


            genderButtonsRefresh();
        } else {
            SharedData.user = new UserModel();
        }
    }


    private void genderButtonsRefresh() {
        switch (gender) {
            case 0: //female
                female.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimaryMidDark)));
                female.setTextColor(ContextCompat.getColor(this, R.color.whiteCardColor));
                female.setStrokeWidth(0);

                male.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
                male.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGray));
                male.setStrokeWidth(2);
                break;
            case 1: //male
                male.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimaryMidDark)));
                male.setTextColor(ContextCompat.getColor(this, R.color.whiteCardColor));
                male.setStrokeWidth(0);

                female.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
                female.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGray));
                female.setStrokeWidth(2);
                break;
        }
    }

    private boolean checkReadPermission() {
        int permissionWriteExternal = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionWriteExternal != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            }
        }
    }

    private void pickImage() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            avatar.setImageURI(imageUri);
        }
    }

    @Override
    public void onValidationSucceeded() {
        user.setName(name.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.setGender(gender);
        user.setUserType(2);
        SharedData.user = user;
        if (imageUri != null) {
            loadingHelper.showLoading("");
            new UploadController().uploadImage(imageUri, new StringCallback() {
                @Override
                public void onSuccess(String text) {
                    loadingHelper.dismissLoading();
                    user.setImage(text);
                    SharedData.user = user;

                    Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                    intent.putExtra("from", 1);
                    intent.putExtra("phone", user.getPhone());
                    startActivity(intent);
                }

                @Override
                public void onFail(String error) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        } else if (isEditing) {
            loadingHelper.showLoading("");
            new UserController().save(user, new UserCallback() {
                @Override
                public void onSuccess(ArrayList<UserModel> users) {
                    loadingHelper.dismissLoading();
                    onBackPressed();
                }

                @Override
                public void onFail(String error) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(RegisterActivity.this, "Pick your profile picture!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }

    }
}