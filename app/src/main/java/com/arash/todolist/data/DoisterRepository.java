package com.arash.todolist.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.arash.todolist.model.Task;
import com.arash.todolist.util.TaskRoomDatabase;

import java.util.List;

public class DoisterRepository {
    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;

    public DoisterRepository(Application application) {
        TaskRoomDatabase database = TaskRoomDatabase.getDatabase(application);
        this.taskDao = database.taskDao();
        this.allTasks = taskDao.getTasks();
    }

    public LiveData<List<Task>> getAllTasks () {
        return allTasks;
    }

    public LiveData<List<Task>> getSpecificTasks ( String s ) {
        return taskDao.getSpecificTasks(s);
    }

    public LiveData<List<Task>> getDoneTasks ( String d ) {
        return taskDao.getDoneTasks(d);
    }

    public LiveData<List<Task>> getSpecificDate ( String s1 , String s2 ) {
        return taskDao.getSpecificDates(s1 , s2);
    }

    public LiveData<Task> get (long id){
        return taskDao.get(id);
    }
    public void insert( Task task){
        TaskRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.insertTask(task);
            }
        });
    }
    public void update (Task task){
        TaskRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.update(task);
            }
        });
    }
    public void delete (Task task){
        TaskRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.delete(task);
            }
        });
    }




}
