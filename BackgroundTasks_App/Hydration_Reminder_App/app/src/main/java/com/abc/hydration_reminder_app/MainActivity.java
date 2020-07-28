package com.abc.hydration_reminder_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.hydration_reminder_app.sync.ReminderTask;
import com.abc.hydration_reminder_app.sync.ReminderUtilities;
import com.abc.hydration_reminder_app.sync.WaterReminderIntentService;
import com.abc.hydration_reminder_app.utilities.NotificationUtils;
import com.abc.hydration_reminder_app.utilities.PreferenceUtilities;

import java.security.PublicKey;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    //Member variables of our Widgets
    public TextView mWaterCountDisplay;
    public TextView mChargingCountDisplay;
    public ImageView mChargingImageView;
    //Toast to show when user touch on the Glass icon
    private Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting the views just daclared above
        mWaterCountDisplay = findViewById(R.id.tv_water_count);
       mChargingCountDisplay =  findViewById(R.id.tv_charging_reminder_count);
        mChargingImageView =  findViewById(R.id.iv_power_increment);

        /** Set the original values in the UI **/
        UpdateWaterCount();
        updateCharingReminderCount();

        /** Setup the shared preference listener **/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        //Calling the Scheduled Job in onCreat()
        ReminderUtilities.scheduleChargingReminder(this);

    }

    /**
     * Updates the TextView to display the new water count from SharedPreferences
     */
    private void UpdateWaterCount(){
        Log.i("Water_Count","Water Counted..");
        int waterCount = PreferenceUtilities.getWaterCount(this);
        mWaterCountDisplay.setText(waterCount+ "");
    }


    /**
     * Updates the TextView to display the new charging reminder count from SharedPreferences
     */

    private void updateCharingReminderCount(){
        int chargingReminders = PreferenceUtilities.getChargingReminderCount(this);
        String formattedChargingReminders = getResources().getQuantityString(
                R.plurals.charge_notification_count, chargingReminders, chargingReminders);
        mChargingCountDisplay.setText(formattedChargingReminders);
    }


    /**
     * Adds one to the water count and shows a toast
     */
    public void incrementWater(View view) {
        Log.i("Glass", "Glass clicked");
        if (mToast != null) mToast.cancel();
        Toast.makeText(this, R.string.water_chug_toast, Toast.LENGTH_SHORT).show();

        Intent incrementWaterCountIntent = new Intent(this, WaterReminderIntentService.class);
        incrementWaterCountIntent.setAction(ReminderTask.ACTION_INCREMENT_WATER_COUNT);
        startService(incrementWaterCountIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

/** Cleanup the shared preference listener **/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * This is a listener that will update the UI when the water count or charging reminder counts
     * change
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PreferenceUtilities.KEY_WATER_COUNT.equals(key)){
            UpdateWaterCount();
        }
        else if (PreferenceUtilities.KEY_CHARGING_REMINDER_COUNT.equals(key)){
            updateCharingReminderCount();
        }
    }


}