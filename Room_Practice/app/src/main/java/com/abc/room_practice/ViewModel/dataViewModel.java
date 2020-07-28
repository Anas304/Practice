package com.abc.room_practice;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.abc.room_practice.Room.DB;
import com.abc.room_practice.Room.TaskEntry;

import java.util.List;

public class dataViewModel extends AndroidViewModel {
    //Constant for logging
    private static final String TAG = dataViewModel.class.getSimpleName();
    //instance variable.
    private LiveData<List<TaskEntry>> tasks;
    public dataViewModel(@NonNull Application application) {
        super(application);
        //instance of our database
        DB maindatabase = DB.getInstance(this.getApplication());
        Log.i(TAG,"Actively retrieving tasks from the DataBase");
        tasks = maindatabase.taskDao().loadAllTask();
    }
    //Getter method for our tasks in dataviewModel class.
    public LiveData<List<TaskEntry>> getTasks() {
        return tasks;
    }
}
