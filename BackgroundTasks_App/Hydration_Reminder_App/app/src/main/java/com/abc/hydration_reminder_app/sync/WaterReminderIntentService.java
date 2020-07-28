package com.abc.hydration_reminder_app.sync;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

//IntentService to manage background syncing
public class WaterReminderIntentService extends IntentService {

    public WaterReminderIntentService() {
        super("WaterReminderIntentService");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //String Action for our ReminderTask.execute method
        String action = intent.getAction();
        ReminderTask.executeTask(this,action);

    }
}
