package com.arash.todolist.model;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.arash.todolist.data.DoisterRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    public static DoisterRepository repository;
    public final LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new DoisterRepository(application);
        allTasks = repository.getAllTasks();
    }

    public  LiveData<List<Task>> getAllTasks() {
        return allTasks ;
    }
    public LiveData<List<Task>> getSpecificTasks ( String s ) {
        return repository.getSpecificTasks(s);
    }
    public LiveData<List<Task>> getDoneTasks ( String d ) {
        return repository.getDoneTasks(d);
    }
    public LiveData<List<Task>> getSpecificData ( String s1 , String s2 ) {
        return repository.getSpecificDate(s1 , s2);
    }
    public LiveData<Task> get (long id){
        return repository.get(id);
    }
    public static void insert(Task task) {
        repository.insert(task);
    }
    public static void update(Task task){
        repository.update(task);
    }
    public static void delete(Task task){
        repository.delete(task);
    }
}
