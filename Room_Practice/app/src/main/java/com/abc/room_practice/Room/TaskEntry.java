package com.abc.room_practice.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
@Entity(tableName = "Task")
public class TaskEntry {
    /**Fields for description,priority,Date and id.
     * All Fields will become Coulumns of the table (unless @Ignore)*/

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int priority;
    private String description;
    //This variable needs TypeConverters.So lets make a new class
    /**Type Converters
     If room cannot figure out how to save one of the fields (like date) into our database we should
     use @TypeConverter.
     Date values in a database should be store only as text, real or integer.
     Room needs to map each of our fields to one of the database data types. Room cannot
     automatically map more complex extractors like dates. */
    private Date Latestupdate;

//Constructor of our TaskEntry Class
public TaskEntry(String description,int id,int priority,Date Latestupdate){
    this.description = description;
    this.id = id;
    this.priority = priority;
    this.Latestupdate = Latestupdate;
}
/** Contructor below is used when we want our app to autoGenerate the id for us.
 * That's why we Ignorig it.*/
    @Ignore
    public TaskEntry(String description, int priority, Date Latestupdate) {
        this.description = description;
        this.priority = priority;
        this.Latestupdate = Latestupdate;
    }

    /**
     * Getters and setters encapsulate the fields of a class by making them accessible only through
     * its public methods and keep the values themselves private. That is considered a good
     * OO principle. ... Getter and setter method are used to get and set the value of Variables
     * which is the way to achive encapsulation.
     */

    public int getId() {
        return id;
    }

    public void setId(int newid) {
        this.id = newid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int newpriority) {
        this.priority = newpriority;
    }

    public Date getLatestupdate() {
        return Latestupdate;
    }

    public void setLatestupdate(Date newLatestupdate) {
        this.Latestupdate = newLatestupdate;
    }
}