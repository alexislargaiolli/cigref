package com.sherpa.mynelis.cigref.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sherpa.mynelis.cigref.R;


public class PreferencesManager {

    private static SharedPreferences sharedPref;

    public static String getStringPreference(Context context, String key) {
        sharedPref = context.getSharedPreferences(context.getString(R.string.sp_collection), Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static void setStringPreference(Context context, String key, String value) {
        sharedPref = context.getSharedPreferences(context.getString(R.string.sp_collection), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean getBooleanPreference(Context context, String key) {
        sharedPref = context.getSharedPreferences(context.getString(R.string.sp_collection), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }

    public static void setBooleanPreference(Context context, String key, boolean value) {
        sharedPref = context.getSharedPreferences(context.getString(R.string.sp_collection), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}