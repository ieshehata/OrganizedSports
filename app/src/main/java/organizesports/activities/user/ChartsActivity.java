package organizesports.activities.user;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.organizesports.R;
import com.app.organizesports.adapters.ChartDataAdapter;
import com.app.organizesports.callbacks.DayCallback;
import com.app.organizesports.controllers.DayController;
import com.app.organizesports.models.DayModel;
import com.app.organizesports.models.listviewitems.BarChartItem;
import com.app.organizesports.models.listviewitems.ChartItem;
import com.app.organizesports.models.listviewitems.LineChartItem;
import com.app.organizesports.utils.LoadingHelper;
import com.app.organizesports.utils.SharedData;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class ChartsActivity extends AppCompatActivity {
    ArrayList<ChartItem> list = new ArrayList<>();
    ListView lv;
    TextView feedback;

    private LoadingHelper loadingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        setTitle("User Charts");
        lv = findViewById(R.id.listView);
        feedback = findViewById(R.id.feedback);

        loadingHelper = new LoadingHelper(this);

        if(SharedData.currentUser.getDays().size() < 7) {
            Toast.makeText(ChartsActivity.this, "charts is better for more than week of use", Toast.LENGTH_LONG).show();
        }

        fetchData();
    }

    private void fetchData() {
        loadingHelper.showLoading("");
        new DayController().getDays(SharedData.currentUser.getKey(), new DayCallback() {
            @Override
            public void onSuccess(ArrayList<DayModel> days) {
                loadingHelper.dismissLoading();
                SharedData.currentUser.setDays(days);
                prepareCharts();
            }

            @Override
            public void onFail(String error) {
                loadingHelper.dismissLoading();
            }
        });
    }

    private void prepareCharts() {
        SharedData.currentUser.getDays().sort((o1, o2)
                -> o1.getDay().compareTo(
                o2.getDay()));

        getFeedback();

        BarChartItem workoutChart =  new BarChartItem(getWorkoutData(), this);
        list.add(workoutChart);

        LineChartItem bmiChart = new LineChartItem(getBMIData(), this);
        list.add(bmiChart);

        BarChartItem waterChart =  new BarChartItem(getWaterData(), this);
        list.add(waterChart);

        BarChartItem walkingChart =  new BarChartItem(getWalkingData(), this);
        list.add(walkingChart);

        ChartDataAdapter cda = new ChartDataAdapter(this, list);
        lv.setAdapter(cda);
    }

    @SuppressLint("DefaultLocale")
    private void  getFeedback() {
        if(SharedData.currentUser.getDays().size() > 1) {
            String f = "";
            DayModel first = SharedData.currentUser.getDays().get(0);
            DayModel last = SharedData.currentUser.getDays().get(SharedData.currentUser.getDays().size() - 1);

            double bmiDiff = last.getBmi() / first.getBmi();
            double waterDiff = last.getWaterCups() / first.getWaterCups();
            double walkingDiff = last.getWalkedDistance() / first.getWalkedDistance();
            double workoutDiff = last.getMinOfTraining() / first.getMinOfTraining();

            if (workoutDiff > 1) {
                f += String.format("\u2022 Workout Time has increased since your first day by %.2f%% Great!\n", (workoutDiff - 1) * 100);
            }else {
                f += String.format("\u2022 Workout Time has decreased since your first day by %.2f%% try harder!\n", (1 - workoutDiff) * 100);
            }

            if (bmiDiff > 1) {
                f += String.format("\u2022 BMI has increased since your first day by %.2f%% try harder!\n", (bmiDiff - 1) * 100);
            }else {
                f += String.format("\u2022 BMI has decreased since your first day by %.2f%% Good Job\n", (1 - bmiDiff) * 100);
            }

            if (waterDiff > 1) {
                f += String.format("\u2022 Water Cups per day has increased since your first day by %.2f%% Good Job\n", (waterDiff - 1) * 100);
            }else {
                f += String.format("\u2022 Water Cups per day has decreased since your first day by %.2f%% drink more!\n", (1 - waterDiff) * 100);
            }

            if (walkingDiff > 1) {
                f += String.format("\u2022 Walking Distance has increased since your first day by %.2f%% Keep Going!", (walkingDiff - 1) * 100);
            }else {
                f += String.format("\u2022 Walking Distance has decreased since your first day by %.2f%% try harder!", (1 - walkingDiff) * 100);
            }



            feedback.setText(f);
        }else {
            feedback.setText("No Enough data to get feedback");
        }
    }
    private LineData getBMIData() {

        ArrayList<Entry> values = new ArrayList<>();

        for(DayModel day : SharedData.currentUser.getDays()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(day.getDay());
            values.add(new Entry(cal.get(Calendar.DAY_OF_YEAR) + 1, (int) day.getBmi()));
        }

        LineDataSet d1 = new LineDataSet(values, "BMI Chart");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        return new LineData(sets);
    }

    private BarData getWaterData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (DayModel day : SharedData.currentUser.getDays()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(day.getDay());
            entries.add(new BarEntry(cal.get(Calendar.DAY_OF_YEAR) + 1, day.getWaterCups()));
        }

        BarDataSet d = new BarDataSet(entries, "Water Chart");
        d.setColors(ColorTemplate.PASTEL_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    private BarData getWalkingData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (DayModel day : SharedData.currentUser.getDays()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(day.getDay());
            entries.add(new BarEntry(cal.get(Calendar.DAY_OF_YEAR) + 1, (int) day.getWalkedDistance()));
        }

        BarDataSet d = new BarDataSet(entries, "Walking Chart");
        d.setColors(ColorTemplate.PASTEL_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    private BarData getWorkoutData() {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (DayModel day : SharedData.currentUser.getDays()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(day.getDay());
            entries.add(new BarEntry(cal.get(Calendar.DAY_OF_YEAR) + 1, day.getMinOfTraining()));
        }

        BarDataSet d = new BarDataSet(entries, "Workout Chart");
        d.setColors(ColorTemplate.PASTEL_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    public static String addBulletPoint(String string) {
        return ('\u2022' + " " + string);
    }
}