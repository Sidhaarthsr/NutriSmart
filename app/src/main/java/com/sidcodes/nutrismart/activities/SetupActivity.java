package com.sidcodes.nutrismart.activities;

//import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.NavController;
//import androidx.navigation.fragment.NavHostFragment;
//import androidx.navigation.ui.NavigationUI;
import com.sidcodes.nutrismart.R;
import com.sidcodes.nutrismart.model.StepData;
import com.sidcodes.nutrismart.utils.SetupVerticalStepperAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//import com.sidcodes.nutrismart.fragments.BioDataFragment;
//import com.sidcodes.nutrismart.fragments.NutritionalRequirementsFragment;
//import com.sidcodes.nutrismart.fragments.ShoppingFrequencyFragment;
//import com.sidcodes.nutrismart.fragments.VendorsFragment;
//import com.sidcodes.nutrismart.utils.PreferenceManager;

public class SetupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // List of steps
        List<StepData> steps = Arrays.asList(new StepData("Bio Data", Arrays.asList("Name")),
                new StepData("Nutritional Requirements", Arrays.asList("Calories", "Protein", "Carbs", "Fat")),
                new StepData("Vendors", Arrays.asList("Vendor 1", "Vendor 2", "Vendor 3")),
                new StepData("Shopping Frequency", Collections.singletonList("Frequency")));

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.stepper_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SetupVerticalStepperAdapter adapter = new SetupVerticalStepperAdapter(steps, recyclerView);
        recyclerView.setAdapter(adapter);


//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.nav_host_fragment);
//        NavController navController = navHostFragment.getNavController();
//        NavigationUI.setupActionBarWithNavController(this, navController);
//
//        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//            if (destination.getId() == R.id.setupCompleteFragment) {
//                new PreferenceManager(this).setSetupComplete(true);
//                startActivity(new Intent(SetupActivity.this, MainActivity.class));
//                finish();
//            }
//        });
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = NavHostFragment.findNavController(
//                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));
//        return navController.navigateUp() || super.onSupportNavigateUp();
//    }

//    public void navigateToStep(int stepIndex) {
//        Fragment fragment;
//        switch (stepIndex) {
//            case 0:
//                fragment = new BioDataFragment();
//                break;
//            case 1:
//                fragment = new NutritionalRequirementsFragment();
//                break;
//            case 2:
//                fragment = new VendorsFragment();
//                break;
//            case 3:
//                fragment = new ShoppingFrequencyFragment();
//                break;
//            default:
//                fragment = new BioDataFragment();
//                break;
//        }
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, fragment)
//                .commit();
//    }
}