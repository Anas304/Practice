package com.abc.hydration_reminder_app.sync;

import android.app.job.JobParameters;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import com.firebase.jobdispatcher.JobService;

import androidx.annotation.RequiresApi;

public class WaterReminderFirebaseJobService extends JobService {
    private AsyncTask mBackgorundTask;
    @Override
    public boolean onStartJob(final com.firebase.jobdispatcher.JobParameters job) {
        mBackgorundTask = new AsyncTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = WaterReminderFirebaseJobService.this;
                ReminderTask.executeTask(context,ReminderTask.ACTION_CHARGING_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                //JobService needs to tell the System when the Job is done by calling jobFinished.
                //The Job is done when the AsyncTask is done and the AsyncTask is done when
                // onPostExecute gets called.
                jobFinished(job,false);
                super.onPostExecute(o);
            }
        };
        mBackgorundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        if (mBackgorundTask != null) mBackgorundTask.cancel(true);
        //true means when the conditions are remet the Job should be retried again.
        return true;
    }
}
