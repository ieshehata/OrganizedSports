package organizesports.activities.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.organizesports.R;
import com.app.organizesports.activities.admin.AdminHomeActivity;
import com.app.organizesports.activities.user.UserHomeActivity;
import com.app.organizesports.callbacks.UserCallback;
import com.app.organizesports.controllers.UserController;
import com.app.organizesports.models.UserModel;
import com.app.organizesports.utils.LoadingHelper;
import com.app.organizesports.utils.SharedData;
import com.app.organizesports.utils.Utils;
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

    private TextInputLayout phoneLayout;
    private String loginPhone, loginPassword;
    private SharedPreferences sharedPref;
    private LoadingHelper loadingHelper;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        phoneLayout = findViewById(R.id.login_phone_field);
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

        if(SharedData.userType == 1) {
            phoneLayout.setHint(R.string.username);
            phone.setInputType(InputType.TYPE_CLASS_TEXT);
            forgetPassword.setVisibility(View.GONE);
            register.setVisibility(View.GONE);
            phoneLayout.setPrefixText("");
            if(SharedData.isTesting) {
                login();
            }
        }else {
            phoneLayout.setHint(R.string.phone);
            if(isSaved) {
                loginPhone = sharedPref.getString(SharedData.PHONE, "");
                loginPassword = sharedPref.getString(SharedData.PASS, "");
                login();
            }else if(SharedData.isTesting) {
                loginPhone = "a@a.com";
                loginPassword = "asd123";
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
            startActivity(intent);
        });
    }

    private void login() {
        Utils.hideKeyboard(LoginActivity.this);
        UserModel user = new UserModel(loginPhone, loginPassword, SharedData.userType);

        if(SharedData.userType == 1) {
            if ((phone.getText().toString().equals("admin") && loginPassword.equals("123456")) || SharedData.isTesting) {
                SharedData.currentUser = SharedData.adminUser;
                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else {
                Toast.makeText(LoginActivity.this, "wrong credential", Toast.LENGTH_LONG).show();
            }
        }else if(SharedData.userType == 2){
            loadingHelper.showLoading("");
            new UserController().checkLogin(user, new UserCallback() {
                @Override
                public void onSuccess(ArrayList<UserModel> users) {
                    if(users.size() > 0) {
                        if(users.get(0).getActivated() == 1) {
                            SharedData.currentUser = users.get(0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean(SharedData.IS_USER_SAVED, true);
                            editor.putString(SharedData.PHONE, loginPhone);
                            editor.putString(SharedData.PASS, loginPassword);
                            editor.apply();
                            loadingHelper.dismissLoading();
                            Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            loadingHelper.dismissLoading();
                            Toast.makeText(LoginActivity.this, "Admin has blocked you from using the app!", Toast.LENGTH_LONG).show();
                        }

                    }else {
                        loadingHelper.dismissLoading();
                        Toast.makeText(LoginActivity.this,
                                R.string.login_error, Toast.LENGTH_LONG).show();
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
        loginPhone = "+965" + phone.getText().toString();
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