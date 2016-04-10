package pl.grudowska.feedme.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    public static void saveDataString(Context context, String preference, String value) {
        SharedPreferences settings = context.getSharedPreferences(preference, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(preference, value);
        editor.apply();

        //Log.i("SPManager ", "saveDataString() " + "PREFERENCE: " + preference + ": " + value);
    }

    public static String loadDataString(Context context, String preference, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(preference, 0);
        String data = settings.getString(preference, defaultValue);

        //Log.i("SPManager ", "loadDataString() " + "PREFERENCE: " + preference + ": " + data);

        return data;
    }

    public static void saveDataInt(Context context, String preference, int value) {
        SharedPreferences settings = context.getSharedPreferences(preference, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(preference, value);
        editor.apply();

        //Log.i("SPManager ", "saveDataString() " + "PREFERENCE: " + preference + ": " + value);
    }

    public static int loadDataInt(Context context, String preference, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(preference, 0);
        int data = settings.getInt(preference, defaultValue);

        //Log.i("SPManager ", "loadDataString() " + "PREFERENCE: " + preference + ": " + data);

        return data;
    }
}
