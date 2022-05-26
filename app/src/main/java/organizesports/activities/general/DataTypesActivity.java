package organizesports.activities.general;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.app.organizesports.R;
import com.app.organizesports.utils.SharedData;

public class DataTypesActivity extends AppCompatActivity {

    LinearLayout exercises, supplements, recipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_types);

        exercises = findViewById(R.id.exercises);
        supplements = findViewById(R.id.supplements);
        recipes = findViewById(R.id.recipes);

        exercises.setOnClickListener(v -> {
            SharedData.currentDataType = 1;
            startActivity(new Intent(this, ExerciseTypesActivity.class));
        });

        supplements.setOnClickListener(v -> {
            SharedData.currentDataType = 2;
            startActivity(new Intent(this, SupplementsActivity.class));
        });

        recipes.setOnClickListener(v -> {
            SharedData.currentDataType = 3;
            startActivity(new Intent(this, RecipesActivity.class));
        });






    }
}