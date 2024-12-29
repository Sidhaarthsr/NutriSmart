package com.sidcodes.nutrismart.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.sidcodes.nutrismart.R;
import com.sidcodes.nutrismart.utils.PreferenceManager;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findViewById(R.id.signupButton).setOnClickListener(v -> {
            PreferenceManager preferenceManager = new PreferenceManager(this);
            if (preferenceManager.isSetupComplete()) {
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SignupActivity.this, SetupActivity.class));
            }
            finish();
        });
    }
}