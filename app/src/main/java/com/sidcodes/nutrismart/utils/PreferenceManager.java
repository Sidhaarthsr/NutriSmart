package com.sidcodes.nutrismart.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREF_NAME = "NutriSmartPreferences";
    private static final String KEY_IS_SETUP_COMPLETE = "is_setup_complete";

    private final SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isSetupComplete() {
        return sharedPreferences.getBoolean(KEY_IS_SETUP_COMPLETE, false);
    }

    public void setSetupComplete(boolean isComplete) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_SETUP_COMPLETE, isComplete);
        editor.apply();
    }
}