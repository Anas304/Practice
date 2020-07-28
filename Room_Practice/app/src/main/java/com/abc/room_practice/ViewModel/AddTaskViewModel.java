package com.abc.room_practice.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.abc.room_practice.Room.DB;
import com.abc.room_practice.Room.TaskEntry;

import java.util.List;

public class AddTaskViewModel extends ViewModel {
    //LiveData memeber variable
    public LiveData<TaskEntry> task;
    public LiveData<TaskEntry> getTask(){return task;}

    public AddTaskViewModel(DB database , int id) {
        task = database.taskDao().loadTaskbyid(id);

    }
}
