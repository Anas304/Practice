package com.abc.hydration_reminder_app.utilities;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.abc.hydration_reminder_app.MainActivity;
import com.abc.hydration_reminder_app.R;
import com.abc.hydration_reminder_app.sync.ReminderTask;
import com.abc.hydration_reminder_app.sync.WaterReminderIntentService;

import java.net.Inet4Address;

public class NotificationUtils {
    //Constants for our notification helper methods
    private static final int WATER_REMINDER_NOTIFICATION_ID = 3816;
    private static final int WATER_REMINDER_PENDING_INTENT_ID = 3417;
    private static final int ACTION_PENDING_INTNET_ID = 3615;
    private static final String
            //Our string for the increment action
            WATER_REMINDER_NOTIFICATION_CHANNEL_ID="reminder_notification_channel";

    //Method to Clear_Up the Notification
    public static void ClearAllNotifcation(Context context){
        NotificationManager ClearnotificationManager = ( NotificationManager )
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        ClearnotificationManager.cancelAll();
    }

    //First method to notify the user with Notification when Charging.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void UserAltertWhenCharing(Context context){
        //As we all know that in Android Oreo, Developers are not allowed to creat Notifiactions
        //Without it belonging to a Notification Channel.
        //So lets creat one.But First we need some Reference from The SystemServices
        // To Access NotificationManager.Okay, Lets do it first
        NotificationManager notificationManager = ( NotificationManager )
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        //For Oreo Devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Now lets creat our notifiaction Channel
            @SuppressLint({"StringFormatInvalid", "LocalSuppress"})
            NotificationChannel mChannelWaterReminderNotif = new NotificationChannel(
                    WATER_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            //assert is a Java keyword used to define an assert statement. An assert statement is used
            // to declare an expected boolean condition in a program.
            // If the program is running with assertions enabled, then the condition is checked at runtime.
            // If the condition is false, the Java runtime system throws an AssertionError .
            //Andriod Stdio was requiring me to enable assert on my notificationManager.Lets see
            //If it may be helpfull for some reason or not.
            assert notificationManager != null;
            //Finalizing notification Channel creation
            notificationManager.createNotificationChannel(mChannelWaterReminderNotif);

            //Now lets creat the actual notification
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,
                WATER_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setSmallIcon(R.drawable.ic_drink_notification)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string
                        .charging_reminder_notification_body)))
                //Below method is how a notification should be presented to the user
                //It contains notification's Sound,Vibrate and Lights.
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setLargeIcon(largeIcon(context))
                .setContentIntent(contentIntent(context))
                .addAction(DismissNotifcationAction(context))
                .addAction(incrementWaterCountAction(context))
                .setAutoCancel(true);
//For older device than Oreo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);  }

        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID,notificationBuilder.build());
    }
    //Now for the setLargeIcon() and setConentIntent we need separate methods
    //Cuz defining methods inside a method is quite imPractical
    //So lets do this

    // This method is necessary to decode a bitmap needed for the notification.
    private static Bitmap largeIcon(Context context){
//For decodeResource() method we need Resource object to pass in the method
        //So Lets creat that first
        Resources res = context.getResources();
        Bitmap largeicon = BitmapFactory.decodeResource(res,R.drawable.ic_local_drink_black_24px);
        return largeicon;

    }
    //Method Definition for setContentIntent()
    // should return a PendingIntent. This method will create the pending intent which will trigger when
    // the notification is pressed. This pending intent should open up the MainActivity.
    private static PendingIntent contentIntent(Context context){

        Intent startMainActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context,WATER_REMINDER_PENDING_INTENT_ID,startMainActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /*
    * Here I am going to creat two helper methods for handling notifcation Actions
    * First for ignoring the Notification
    * Second for incrementing the WaterCount
    * And Finally im gonna add these two methods
    * into my Notification.Builder method **/
    //Helper methods For Ignoring notification.
    private static NotificationCompat.Action DismissNotifcationAction(Context context){
        //Intent to Launch WaterReminderIntentService
        Intent dismissActionIntent = new Intent(context, WaterReminderIntentService.class);
        //Setting action of the intent
        dismissActionIntent.setAction(ReminderTask.ACTION_DISMISS_NOTIFICATION);
        //PendingIntent to handle background task
        PendingIntent dismissPendingIntent  = PendingIntent.getService(context,ACTION_PENDING_INTNET_ID,
                dismissActionIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        //The Actual Action to be Performed
        NotificationCompat.Action dismissAction  = new NotificationCompat
                .Action(R.drawable.ic_cancel_black_24px,"No Thanks",dismissPendingIntent);
        //Returning the Action.
        return dismissAction;
    }
    private static NotificationCompat.Action incrementWaterCountAction(Context context){
        //Intent to Launch WaterReminderIntentService
        Intent incrementActionIntent = new Intent(context, WaterReminderIntentService.class);
        //Setting action of the intent
        incrementActionIntent.setAction(ReminderTask.ACTION_INCREMENT_WATER_COUNT);
        //PendingIntent to handle background task
        PendingIntent incrementPendingIntent  = PendingIntent.getService(context,ACTION_PENDING_INTNET_ID,
                incrementActionIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        //The Actual Action to be Performed
        NotificationCompat.Action incrementAction  = new NotificationCompat
                .Action(R.drawable.ic_drink_notification,"I Did It",incrementPendingIntent);
        //Returning the Action.
        return incrementAction;
    }




}
