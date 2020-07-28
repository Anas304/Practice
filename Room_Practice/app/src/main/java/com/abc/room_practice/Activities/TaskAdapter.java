package com.abc.room_practice;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.abc.room_practice.Room.TaskEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * This TaskAdapter creates and binds ViewHolders, that hold the description and priority of a task,
 * to a RecyclerView to efficiently display data.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskviewHolder> {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";

    // Member variable to handle item clicks
    private final ItemClickListener itemclicked;
    // Class variables for the List that holds task data and the Context
    private List<TaskEntry> mTaskEntries;
    private Context mContext;
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    /**
     * Constructor for the TaskAdapter that initializes the Context.
     *
     * @param context             the current Context
     * @param listener the ItemClickListener
     */
    public TaskAdapter(Context context, ItemClickListener listener) {
        this.mContext = context;
        this.itemclicked = listener;
    }
    /**In order to get the private list of tasks we need to get a public getTask() method
     * to our adapter.Using this method we can retrieve the list of taskEntry Objects
     * from our adapter.To get the exact task we need to know which position was swiped.So
     * Lets go to the MainActivity.java*/
    public List<TaskEntry> getTasks(){
        return mTaskEntries;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @NonNull
    @Override
    public TaskviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.task_layout, parent, false);

        return new TaskviewHolder(view);
        /** Here I was fighting with an error that says Required type: Context and Provided :View
         * I was confused that what the hell is the context.So i googled it understand its  meaning
         * and then Found the statement below which i really like and thought to include it in
         * my project.So here it is:
         * It's like access of android activity to the app's resource.

         It's similar to when you visit a hotel, you want breakfast, lunch & dinner in the suitable
         timings, right?There are many other things you like during the time of stay. How do you
         get these things?You ask the room-service person to bring these things for you.

         Here the room-service person is the "context" considering you are the single activity and
         the hotel to be your app, finally the breakfast, lunch & dinner have to be the resources.*/
    }


    @Override
    public void onBindViewHolder(TaskviewHolder holder, int position) {
        // Determine the values of the wanted data
        TaskEntry taskEntry = mTaskEntries.get(position);
        String description = taskEntry.getDescription();
        int priority = taskEntry.getPriority();
        String Latestupdate = dateFormat.format(taskEntry.getLatestupdate());

        //Set values
        holder.taskDescriptionView.setText(description);
        holder.updatedAtView.setText(Latestupdate);

        // Programmatically set the text and color for the priority TextView
        String priorityString = "" + priority; // converts int to String
        ((TaskviewHolder) holder).priorityView.setText(priorityString);

        GradientDrawable priorityCircle = (GradientDrawable) holder.priorityView.getBackground();
        // Get the appropriate background color based on the priority
        int priorityColor = getPriorityColor(priority);
        priorityCircle.setColor(priorityColor);

    }
    /**
     Helper method for selecting the correct priority circle color.
     P1 = red, P2 = orange, P3 = yellow
     */
    private int getPriorityColor(int priority) {
        int priorityColor = 0;
        switch (priority){
            case 1:
                priorityColor = ContextCompat.getColor(mContext,R.color.materialRed);
                break;
            case 2:
                priorityColor = ContextCompat.getColor(mContext,R.color.materialOrange);
                break;
            case 3:
                priorityColor = ContextCompat.getColor(mContext,R.color.materialYellow);
                break;
            default:
                break;
        }
        return priorityColor;
    }

    @Override
    public int getItemCount() {
        if (mTaskEntries == null){
            return 0;}
        return mTaskEntries.size();
    }
    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setTasks(List<TaskEntry> taskEntries) {
        mTaskEntries = taskEntries;
        notifyDataSetChanged();
    }
    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }


    public class TaskviewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Class variables for the task description and priority TextViews
        TextView taskDescriptionView;
        TextView updatedAtView;
        TextView priorityView;

        public TaskviewHolder(View itemView) {
            super(itemView);
            taskDescriptionView = itemView.findViewById(R.id.taskDescription);
            updatedAtView = itemView.findViewById(R.id.taskUpdatedAt);
            priorityView = itemView.findViewById(R.id.priorityTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /** To know about the methods used below
             * you can rewrite the method and wait for the details that pop-up
             * to see what the regarding method does*/
            int taskEntry_Variable = mTaskEntries.get(getAdapterPosition()).getId();
            itemclicked.onItemClickListener(taskEntry_Variable);
        }
    }
}