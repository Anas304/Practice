package com.abc.hydration_reminder_app.utilities;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * This class contains utility methods which update water and charging counts in SharedPreferences
 * SharePreferences perform action in key/value pairs.
 * SharePreferences are used to perform user-oriented actions
 */
public class PreferenceUtilities {
    //Constants for keys for the sharePreferences
    //Keys
    public static final String KEY_WATER_COUNT = "water-count";
    //We talk about this one later.
    public static final String KEY_CHARGING_REMINDER_COUNT = "charging-reminder-count";
    //Values
    private static final int DEFAULT_COUNT = 0;

    //Setting the Watercount to Update the Value of Key_water_count
    synchronized private static void setWaterCount(Context context, int glassesOfWater) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_WATER_COUNT, glassesOfWater);
        editor.apply();
    }
    //Getting the WaterCount to read the updated value
    public static int getWaterCount(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY_WATER_COUNT,DEFAULT_COUNT);
    }
    /** Now we that we have our setter and getter methods for
     * updating and reading updated values
     * We need a method to increment waterCount in our UI */
//Helper method to increment Water Count
    synchronized public static void incrementWaterCount(Context context){
        int waterCount = PreferenceUtilities.getWaterCount(context);
        PreferenceUtilities.setWaterCount(context,++waterCount);
    }
//Helper mothed to increment CharginReminderCount
    synchronized public static void incrementChargingReminderCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingReminders = prefs.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_CHARGING_REMINDER_COUNT, ++chargingReminders);
        editor.apply();
    }

    public static int getChargingReminderCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingReminders = prefs.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT);
        return chargingReminders;
    }
}
