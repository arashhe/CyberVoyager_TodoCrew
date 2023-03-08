package com.arash.todolist.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.arash.todolist.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insertTask (Task task);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Query("SELECT * FROM task_table WHERE is_done == 0")
    LiveData<List<Task>> getTasks();

    @Query("SELECT * FROM task_table WHERE is_done = :d")
    LiveData<List<Task>> getDoneTasks(String d);

    @Query("SELECT * FROM task_table WHERE PRIORITY =  :s")
    LiveData<List<Task>> getSpecificTasks(String s);

    @Query("SELECT * FROM task_table WHERE due_date > :s1 AND due_date <  :s2")
    LiveData<List<Task>> getSpecificDates(String s1, String s2);

    @Query("SELECT * FROM task_table WHERE task_table.task_id == :id")
    LiveData<Task> get (long id);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);
}
