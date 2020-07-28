package com.abc.room_practice;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.abc.room_practice.Executors.AppExecutors;
import com.abc.room_practice.Room.DB;
import com.abc.room_practice.Room.TaskEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**Here we going to make a project that explains architechture components Specially "Room" which
 * is used to handle local data persistence
 * We gonna walk through step by step procedure
 * and along the way we'll learn how to use these new Concepts
 * for our own projects*/
public class MainActivity extends AppCompatActivity implements com.abc.room_practice.TaskAdapter.ItemClickListener {
    //Member variable for the RecyclerView and Adapter
    private RecyclerView mRecyclerView;
    private com.abc.room_practice.TaskAdapter mAdapter;
    private DB mDB;
    //Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerViewTasks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new com.abc.room_practice.TaskAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration
                (getApplicationContext(), DividerItemDecoration.HORIZONTAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

           /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        //code to delete task
                        List<TaskEntry> deltask = mAdapter.getTasks();
                        mDB.taskDao().delete(deltask.get(position));
                        //Calling the method to reflect changes in the UI
                        /** Every change in the database will trigger the onChanged() of the observer
                         * Hence we dont need that anymore*/
                        //RetrieveTasks();
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton fabButton = findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTaskActivityIntent = new Intent(MainActivity.this,
                        com.abc.room_practice.AddTaskActivity.class);
                //In order to make this intent work we need to specify its corresponding Layout
                // in the Manifest.
                startActivity(addTaskActivityIntent);
            }
        });
        mDB = DB.getInstance(getApplicationContext());
        /**Calling RetrieveTasks() method in onCreat means that we need to call this method only
         * when our app gets Created*/
        SetupViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

  /** private void RetrieveTasks() {
        AppExecutors.getInstance()
                .diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<TaskEntry> tasks = mDB.taskDao().loadAllTask();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
  Code to update the UI of Adapter.
                        mAdapter.setTasks(tasks);
                        /**we will learn to simplify this once we learn
                         * Android Architechture Componets then i will comment these lines of code
                         * just to remember how things were done in the past.
                    }
                });

            }
        });
    }
   */    private void SetupViewModel() {
       /**We dont need the line below as we are Quering the database from the ViewModel now. */
     // LiveData<List<TaskEntry>> tasks = mDB.taskDao().loadAllTask();
      com.abc.room_practice.dataViewModel viewModel =  new ViewModelProvider(this)
              .get(com.abc.room_practice.dataViewModel.class);
      viewModel.getTasks().observe(this, new Observer<List<TaskEntry>>() {
          @Override
          public void onChanged( List<TaskEntry> taskEntries) {
              Log.i(TAG,"updating list of tasks from LiveData in ViewModel");
              mAdapter.setTasks(taskEntries);
          }
      });
  }

    @Override
    public void onItemClickListener(int itemId) {
        /**Here we implement our update task Code */
        Intent addtaskintent = new Intent(this, com.abc.room_practice.AddTaskActivity.class);
        addtaskintent.putExtra(com.abc.room_practice.AddTaskActivity.EXTRA_TASK_ID,itemId);
        startActivity(addtaskintent);

    }
}
