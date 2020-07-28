package com.abc.room_practice.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface TaskDao {
    @Query("Select * FROM Task ORDER BY priority")
  LiveData<List<TaskEntry>> loadAllTask();
    @Insert
    void insert(TaskEntry taskEntry);
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(TaskEntry taskEntry);
    @Delete
    void delete(TaskEntry taskEntry);

/** When we want to update a task,we need to call the method below by id.*/
    @Query("SELECT * FROM Task WHERE id = :id")
    LiveData<TaskEntry> loadTaskbyid(int id);
}
