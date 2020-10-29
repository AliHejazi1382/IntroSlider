package com.example.introslider;

import android.content.Context;
import android.content.SharedPreferences;

public class IntroSliderPrefManager {
    private Context context;
    private SharedPreferences preferences;
    private static final String KEY_NAME = "intro";
    private static final String KEY_VALUE = "seen";
    public IntroSliderPrefManager(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE);
    }

    public void setPref(boolean seen) {
        preferences.edit().putBoolean(KEY_VALUE, seen).apply();
    }

    public boolean getPref() {
        return preferences.getBoolean(KEY_VALUE, true);
    }
}
