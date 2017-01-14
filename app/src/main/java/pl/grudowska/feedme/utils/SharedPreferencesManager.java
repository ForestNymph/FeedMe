package pl.grudowska.feedme.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    public static void saveDataString(Context context, String preference, String value) {
        SharedPreferences settings = context.getSharedPreferences(preference, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(preference, value);
        editor.apply();
    }

    public static String loadDataString(Context context, String preference, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(preference, 0);
        return settings.getString(preference, defaultValue);
    }

    public static void saveDataInt(Context context, String preference, int value) {
        SharedPreferences settings = context.getSharedPreferences(preference, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(preference, value);
        editor.apply();
    }

    public static int loadDataInt(Context context, String preference, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(preference, 0);
        return settings.getInt(preference, defaultValue);
    }

    public static void saveDataBoolean(Context context, String preference, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(preference, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(preference, value);
        editor.apply();
    }

    public static boolean loadDataBoolean(Context context, String preference, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(preference, 0);
        return settings.getBoolean(preference, defaultValue);
    }
}
