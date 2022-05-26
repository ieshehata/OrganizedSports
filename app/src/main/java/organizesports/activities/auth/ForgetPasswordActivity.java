package organizesports.activities.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.organizesports.R;
import com.app.organizesports.callbacks.UserCallback;
import com.app.organizesports.controllers.UserController;
import com.app.organizesports.models.UserModel;
import com.app.organizesports.utils.LoadingHelper;
import com.app.organizesports.utils.SharedData;

import java.util.ArrayList;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText phone;
    private String enteredPhone;
    private SharedPreferences sharedPref;
    private LoadingHelper loadingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        phone = findViewById(R.id.phone);
        sharedPref = this.getSharedPreferences(SharedData.PREF_KEY, Context.MODE_PRIVATE);
        loadingHelper = new LoadingHelper(this);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    enteredPhone = phone.getText().toString();

                    new UserController().getUserByPhone(enteredPhone, new UserCallback() {
                        @Override
                        public void onSuccess(ArrayList<UserModel> users) {
                            if(users.size() > 0) {
                                SharedData.currentUser = users.get(0);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean(SharedData.IS_USER_SAVED, true);
                                editor.putString(SharedData.PHONE, users.get(0).getPhone());
                                editor.putString(SharedData.PASS, users.get(0).getPass());
                                editor.apply();

                                loadingHelper.dismissLoading();
                                Intent intent = new Intent(ForgetPasswordActivity.this, ResetPasswordActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(ForgetPasswordActivity.this, "user not registered", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFail(String error) {
                            loadingHelper.dismissLoading();
                            Toast.makeText(ForgetPasswordActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private boolean validate() {
        boolean validData = true;
        if(TextUtils.isEmpty(phone.getText().toString())){
            validData = false;
            phone.setError("required field!");
        }
        if(phone.getText().toString().length() < 10){
            validData = false;
            phone.setError("Phone is not valid");
        }
        return validData;
    }
}