package com.abc.hydration_reminder_app.sync;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.abc.hydration_reminder_app.utilities.NotificationUtils;
import com.abc.hydration_reminder_app.utilities.PreferenceUtilities;

//This Class will be used to increment water count from PreferenceUtilities
public class ReminderTask {
    //To increment water count,I need a variable to hold the increment Key.
    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";
    //Our String for the dismiss Notification
    public static final String
           ACTION_DISMISS_NOTIFICATION = "dismiss_notification_channel";
//New task for charging reminder.
    public static final String
            ACTION_CHARGING_REMINDER = "charging-reminder";

    //Creating a method to execute the method below.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void executeTask(Context context, String action) {
        if (ACTION_INCREMENT_WATER_COUNT.equals(action)) {
            incrementWaterCount(context);
        }
        else if (ACTION_DISMISS_NOTIFICATION.equals(action)){
            NotificationUtils.ClearAllNotifcation(context);
        }
        else if (ACTION_CHARGING_REMINDER.equals(action)){
            issueCharingReminder(context);
        }
    }
//Its gonna make a notification and also increment the chargingRemimder count
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void issueCharingReminder(Context context) {
        PreferenceUtilities.incrementChargingReminderCount(context);
        NotificationUtils.UserAltertWhenCharing(context);

    }
    //Now creating a method called incrementWatercount from PreferenceUtilities Class

    private static void incrementWaterCount(Context context) {
        // From incrementWaterCount, calling the PreferenceUtility
        // method that will ultimately update the water count
        PreferenceUtilities.incrementWaterCount(context);
        //method to dismiss all notifications after we are done.
        NotificationUtils.ClearAllNotifcation(context);
    }
}
//Why? details in the Udacity Lesson:9 Topic:12
