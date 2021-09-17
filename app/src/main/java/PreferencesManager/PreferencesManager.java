package PreferencesManager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String PREFERENCES = "myPrefs";
    private static PreferencesManager instance = null;
    private static SharedPreferences sharedPreferences;

    // Singleton pattern for PreferencesManager
    public static PreferencesManager getInstance() {
        if(instance == null) {
            instance = new PreferencesManager();
        }
        return instance;
    }

    // Singleton pattern for SharedPreferences
    public static SharedPreferences getSharedPreferences(Context context) {
        if(sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(PREFERENCES,context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    // helper method for storing key-value pair into sharedPreference
    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    // helper method for retrieving values from sharedPreference
    public static String getString(Context context,String key, String defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(key, defValue);
    }
}
