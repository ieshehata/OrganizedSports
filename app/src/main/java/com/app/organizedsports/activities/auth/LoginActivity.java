package com.app.organizedsports.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.organizedsports.R;
import com.app.organizedsports.activities.admin.AdminMainActivity;
import com.app.organizedsports.activities.user.UserMainActivity;
import com.app.organizedsports.callback.UserCallback;
import com.app.organizedsports.controllers.UserController;
import com.app.organizedsports.model.UserModel;
import com.app.organizedsports.utils.LoadingHelper;
import com.app.organizedsports.utils.SharedData;
import com.app.organizedsports.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener{
    @NotEmpty
    TextInputEditText phone;

    @NotEmpty
    TextInputEditText password;

    Button login, forgetPassword, register;

    private String loginPhone, loginPassword;
    private SharedPreferences sharedPref;
    private LoadingHelper loadingHelper;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        forgetPassword = findViewById(R.id.forget_pass);
        register = findViewById(R.id.register);
        loadingHelper = new LoadingHelper(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        sharedPref = this.getSharedPreferences(SharedData.PREF_KEY, Context.MODE_PRIVATE);
        boolean isSaved = sharedPref.getBoolean(SharedData.IS_USER_SAVED, false);
        int savedType = sharedPref.getInt(SharedData.USER_TYPE, 0);

        if(SharedData.userType == 1) {
            ((TextInputLayout) findViewById(R.id.login_phone_field)).setHint(R.string.username);
            phone.setInputType(InputType.TYPE_CLASS_TEXT);
            phone.setText("");
            forgetPassword.setVisibility(View.GONE);
            register.setVisibility(View.GONE);
        }else {
            if(isSaved && SharedData.userType == savedType) {
                loginPhone = sharedPref.getString(SharedData.PHONE, "");
                loginPassword = sharedPref.getString(SharedData.PASS, "");
                login();
            }
        }



        forgetPassword.setOnClickListener(v -> {
            Utils.hideKeyboard(LoginActivity.this);
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        });

        // Click button Login
        login.setOnClickListener(view -> {
            validator.validate();
        });

        register.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("isEditing", false);
            startActivity(intent);
        });
    }

    private void login() {
        Utils.hideKeyboard(LoginActivity.this);
        UserModel user = new UserModel(loginPhone, loginPassword,2);

        if(SharedData.userType == 1) {
            if(loginPhone.equals("admin") && loginPassword.equals("123456")) {
                Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else {
                Toast.makeText(LoginActivity.this, "wrong credential", Toast.LENGTH_LONG).show();
            }
        }else if(SharedData.userType == 2) { //user
            loadingHelper.showLoading("");
            new UserController().checkLogin(user, new UserCallback() {
                @Override
                public void onSuccess(ArrayList<UserModel> users) {
                    loadingHelper.dismissLoading();
                    if(users.size() > 0) {
                        loadingHelper.dismissLoading();
                        if(users.get(0).getState() == 1) {
                            SharedData.user = users.get(0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean(SharedData.IS_USER_SAVED, true);
                            editor.putString(SharedData.PHONE, loginPhone);
                            editor.putString(SharedData.PASS, loginPassword);
                            editor.putInt(SharedData.USER_TYPE, SharedData.userType);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Your account not active yet, contact admin to activate!", Toast.LENGTH_LONG).show();
                        }

                    }else {
                        loadingHelper.dismissLoading();
                        Toast.makeText(LoginActivity.this, R.string.login_error, Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFail(String error) {
                    loadingHelper.dismissLoading();
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onValidationSucceeded() {
        loginPhone = phone.getText().toString();
        loginPassword = password.getText().toString();
        login();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}