package com.sidcodes.nutrismart.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.sidcodes.nutrismart.R;
import com.sidcodes.nutrismart.utils.PreferenceManager;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        findViewById(R.id.signupButton).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SetupActivity.class));
        });
    }
}