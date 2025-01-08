package com.sidcodes.nutrismart.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sidcodes.nutrismart.R;
import com.sidcodes.nutrismart.model.StepData;
import com.sidcodes.nutrismart.utils.SetupVerticalStepperAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // List of steps
        List<StepData> steps = Arrays.asList(new StepData("Personal Profile", Arrays.asList("Name", "Age", "Gender", "Height","Weight", "Country", "Lifestyle"), R.drawable.vital_info_microsoft),
                new StepData("Nutritional Requirements", Arrays.asList("Calories", "Protein", "Carbs", "Fat", "Fiber"), R.drawable.nutritional_requirements_microsoft),
                new StepData("Dietary Preferences", Arrays.asList("Vegetarian", "Vegan", "Gluten-Free", "Lactose-Free", "Nut-Free", "Seafood-Free"), R.drawable.dietary_preferences_1_microsoft),
                new StepData("Grocery Partners", Arrays.asList("Vendor 1", "Vendor 2", "Vendor 3"), R.drawable.grocery_partners_1_microsoft),
                new StepData("Grocery Cycle", Collections.singletonList("Frequency"), R.drawable.grocery_cycle_2_microsoft));

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.stepper_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SetupVerticalStepperAdapter adapter = new SetupVerticalStepperAdapter(steps, recyclerView, this);
        recyclerView.setAdapter(adapter);
    }
}