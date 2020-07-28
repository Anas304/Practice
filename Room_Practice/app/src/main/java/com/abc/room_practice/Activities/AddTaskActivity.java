package com.abc.room_practice;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.abc.room_practice.Executors.AppExecutors;
import com.abc.room_practice.Room.DB;
import com.abc.room_practice.Room.TaskEntry;
import com.abc.room_practice.ViewModel.AddTaskViewModel;
import com.abc.room_practice.ViewModel.addTaskViewModelFactory;

import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    // Extra for the task ID to be received in the intent
    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;
    // Constant for logging
    private static final String TAG = AddTaskActivity.class.getSimpleName();
    // Fields for views
    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mButton;

    private int mTaskId = DEFAULT_TASK_ID;
    //Memeber variable for Database
    public DB mDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        initViews();

//Initialized member variable for the data base
        mDB = DB.getInstance(getApplicationContext());
        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }
        final Intent thisActivityintent = getIntent();
        if (thisActivityintent != null && thisActivityintent.hasExtra(EXTRA_TASK_ID)){
            mButton.setText(R.string.update_button);
            if (mTaskId == DEFAULT_TASK_ID){
                mTaskId =  thisActivityintent.getIntExtra(EXTRA_TASK_ID,DEFAULT_TASK_ID);
                /**
                //Use the loadTaskById method to retrieve the task with id mTaskId and
                // assign its value to a final TaskEntry variable
                final LiveData<TaskEntry> task = mDB.taskDao().loadTaskbyid(mTaskId); */
                //As our LiveData will run by default, out of the main thread
                //We dont need runOnUiThread anymore.

                addTaskViewModelFactory factory = new addTaskViewModelFactory(mDB,mTaskId);
                final AddTaskViewModel viewModel = new ViewModelProvider(this,factory)
                        .get(AddTaskViewModel.class);

                viewModel.getTask().observe(this, new Observer<TaskEntry>() {
                    @Override
                    public void onChanged(TaskEntry taskEntry) {
                        //Remove the observer as we do not need it any more
                        viewModel.getTask().removeObserver(this);
                        //UI update code.
                        populateUI(taskEntry);
                    }
                });
          /**
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                            }
                        });
                        /**
                         runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        //upate UI Code.
                        populateUI(task);
                        }
                        });
                    }
                }); */
            }


        }

    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param task the taskEntry to populate the UI
     */
    private void populateUI(TaskEntry task) {
        if (task == null) {
            return;
        }

        // COMPLETED (8) use the variable task to populate the UI
        mEditText.setText(task.getDescription());
        setViewsPriority(task.getPriority());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(INSTANCE_TASK_ID,mTaskId);

    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mEditText = findViewById(R.id.editTextTaskDescription);
        mRadioGroup = findViewById(R.id.radioGroup);

        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }

            private void onSaveButtonClicked() {
                //Whenever user click on the Add Button
                String desciption = mEditText.getText().toString();
                int priority = getViewsPriority();
                Date date = new Date();
                final TaskEntry taskEntry = new TaskEntry(desciption,priority,date);
                //Accessing AppExecutors to handle our database logic
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mTaskId == DEFAULT_TASK_ID){
                            mDB.taskDao().insert(taskEntry);
                        }
                        else{
                            //update Task
                            taskEntry.setId(mTaskId);
                            mDB.taskDao().update(taskEntry);
                        }
                        finish();
                    }
                });


            }

        }); }
    /**
     * getPriority is called whenever the selected priority needs to be retrieved
     */
    public int getViewsPriority() {
        int priority = 1;
        int checkedId = (( RadioGroup ) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
                break;
        }
        return priority;
    }

    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     */
    public int setViewsPriority(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                (( RadioGroup ) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                (( RadioGroup ) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                (( RadioGroup ) findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
        return priority;
    }
}
