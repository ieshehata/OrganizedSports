package organizesports.activities.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.organizesports.R;
import com.app.organizesports.callbacks.DayCallback;
import com.app.organizesports.controllers.DayController;
import com.app.organizesports.dialogs.DateDialog;
import com.app.organizesports.models.DayModel;
import com.app.organizesports.utils.LoadingHelper;
import com.app.organizesports.utils.SharedData;
import com.app.organizesports.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProgressActivity extends AppCompatActivity implements Validator.ValidationListener, DateDialog.DateDialogListener {

    @NotEmpty
    TextInputEditText workout, walk, water, weight, height;

    TextView day, date, bmi;
    Button save, changeDate;


    private LoadingHelper loadingHelper;
    private Validator validator;
    private boolean isEditing = false;
    private DayModel dayModel = new DayModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        setTitle("Day Progress");

        isEditing = getIntent().getBooleanExtra("isEditing", false);

        workout = findViewById(R.id.workout);
        walk = findViewById(R.id.walk);
        water = findViewById(R.id.water);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        day = findViewById(R.id.day);
        date = findViewById(R.id.date);
        bmi = findViewById(R.id.bmi);
        changeDate = findViewById(R.id.change_date);
        save = findViewById(R.id.save);

        loadingHelper = new LoadingHelper(this);
        validator = new Validator(this);
        validator.setValidationListener(this);

        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()) {
                    dayModel.setWeight(Double.parseDouble(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                setBMI();
            }
        });

        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()) {
                    dayModel.setHeight(Double.parseDouble(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                setBMI();
            }
        });

        changeDate.setOnClickListener(v -> {
            DateDialog dialog = new DateDialog("Day Date", null, false);
            dialog.show(getSupportFragmentManager(), "DateTime");
        });

        save.setOnClickListener(v -> {
            validator.validate();
        });
        setData();

    }

    @SuppressLint("DefaultLocale")
    private void setData() {
        if (isEditing) {
            dayModel = SharedData.currentDay;
            workout.setText(String.format("%d", dayModel.getMinOfTraining()));
            walk.setText(String.format("%.2f", dayModel.getWalkedDistance()));
            water.setText(String.format("%d", dayModel.getWaterCups()));
            weight.setText(String.format("%2f", dayModel.getWeight()));
            height.setText(String.format("%2f", dayModel.getHeight()));
        }else {
            if(dayModel.getDay() == null) {
                dayModel.setDay(Utils.justDate(Calendar.getInstance().getTime()));
            }
            dayModel.setUserKey(SharedData.currentUser.getKey());

            workout.setText("");
            walk.setText("");
            water.setText("");
            weight.setText("");
            height.setText("");
        }
        day.setText(Utils.dayName(dayModel.getDay()));
        date.setText(Utils.dayDate(dayModel.getDay()));
        setBMI();
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void setBMI() {
        if(isValidBMI()) {
            double bmiValue = dayModel.getWeight() / Math.pow(dayModel.getHeight() / 100, 2);
            bmi.setText("BMI = " + String.format("%.2f", bmiValue));
            dayModel.setBmi(bmiValue);
        }else {
            bmi.setText("");
            Toast.makeText(this, "Not valid BMI, check weight and height", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isValidBMI() {
        return dayModel.getWeight() > 10 && dayModel.getHeight() > 10;
    }

    @Override
    public void onValidationSucceeded() {
        if(!isValidBMI()) {
            Toast.makeText(this, "Not valid BMI", Toast.LENGTH_LONG).show();
            return;
        }
        dayModel.setMinOfTraining(Integer.parseInt(workout.getText().toString()));
        dayModel.setWaterCups(Integer.parseInt(water.getText().toString()));
        dayModel.setWalkedDistance(Double.parseDouble(walk.getText().toString()));
        dayModel.setWeight(Double.parseDouble(weight.getText().toString()));
        dayModel.setHeight(Double.parseDouble(height.getText().toString()));

        loadingHelper.showLoading("");
        new DayController().save(dayModel, new DayCallback() {
            @Override
            public void onSuccess(ArrayList<DayModel> days) {
                loadingHelper.dismissLoading();
                Toast.makeText(ProgressActivity.this, "Saved!", Toast.LENGTH_LONG).show();
                onBackPressed();
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
                Toast.makeText(ProgressActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void getDate(Date date) {
        isEditing = false;
        for(DayModel dayModel : SharedData.currentUser.getDays()) {
            if(Utils.isSameDay(dayModel.getDay(), date)) {
                isEditing = true;
                SharedData.currentDay = dayModel;
            }
        }
        if(isEditing == false) {
            dayModel = new DayModel();
            dayModel.setDay(Utils.justDate(date));
        }
        setData();
    }
}