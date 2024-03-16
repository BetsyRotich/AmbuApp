package com.example.ambuapp.common;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String prefsName = "default_prefs";
    private static final String ROLE = "ROLE";
    private final SharedPreferences sharedPref;


    public Preferences(Context context) {
        sharedPref = context.getSharedPreferences(
                prefsName, Context.MODE_PRIVATE);
    }

    public String getRole() {
        return sharedPref.getString(ROLE, null);
    }
}
