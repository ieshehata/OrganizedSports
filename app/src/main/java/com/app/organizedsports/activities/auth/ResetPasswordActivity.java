package com.app.organizedsports.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.app.organizedsports.R;
import com.app.organizedsports.callback.UserCallback;
import com.app.organizedsports.controllers.UserController;
import com.app.organizedsports.model.UserModel;
import com.app.organizedsports.utils.LoadingHelper;
import com.app.organizedsports.utils.SharedData;
import com.app.organizedsports.utils.Utils;

import java.util.ArrayList;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText pass, confirmPass;
    private LoadingHelper loadingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        loadingHelper = new LoadingHelper(this);

        pass = findViewById(R.id.pass);
        confirmPass = findViewById(R.id.confirm_pass);
        findViewById(R.id.reset).setOnClickListener(v -> {
            Utils.hideKeyboard(ResetPasswordActivity.this);
            loadingHelper.showLoading("");

            if (validate()) {
                if(SharedData.userType == 2) {
                    SharedData.currentUser.setPassword(pass.getText().toString());
                    new UserController().save(SharedData.currentUser, new UserCallback() {
                        @Override
                        public void onSuccess(ArrayList<UserModel> users) {
                            loadingHelper.dismissLoading();
                            SharedData.currentUser = users.get(0);
                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onFail(String error) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });
    }

    private boolean validate() {
        boolean validData = true;
        if(!pass.getText().toString().equals(confirmPass.getText().toString())) {
            validData = false;
            confirmPass.setError(getString(R.string.no_match));
        }
        if(TextUtils.isEmpty(pass.getText().toString())){
            validData = false;
            pass.setError(getString(R.string.required));
        }
        if(TextUtils.isEmpty(confirmPass.getText().toString())){
            validData = false;
            confirmPass.setError(getString(R.string.required));
        }
        return validData;
    }
}