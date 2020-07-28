package com.abc.room_practice.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.abc.room_practice.Room.DB;

//Important note:
/**We are making these ViewModel Classes Because as u can see there are same classes but they all
 * are activities.As we know Activities are destroyed and recreated everytime there is a
 * configuration change.It means when there is a configuration change there will be a call to the
 * LiveData and hence we are Again Reqeuring the database.Simply "Khoti borr thalley."
 * So that's why we are using the viewmodel cuz ViewModel make the data to be able to Survive
 * Configuration Changes.And Here  that's Exactly what we want.*/

public class addTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    //Memeber variables for the database and task to retrieve it in our ViewModelfactory by id
    // to update.
    private final DB mDB;
    private final int mTaskId;

    public addTaskViewModelFactory(DB mDB, int mTaskId) {
        this.mDB = mDB;
        this.mTaskId = mTaskId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return super.create(modelClass);
    }
}
