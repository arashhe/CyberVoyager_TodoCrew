package com.arash.todolist.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.arash.todolist.data.TaskDao;
import com.arash.todolist.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class TaskRoomDatabase extends RoomDatabase {

    public static final int NUMBER_OF_THREAD = 4;
    private static volatile TaskRoomDatabase INSTANCE ;
    public static final String DATABASE_NAME = "todolist_database";
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool( NUMBER_OF_THREAD);


    public static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriteExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            //invoke Dao and write

                        }
                    });
                }
            };
    public static TaskRoomDatabase getDatabase (final Context context){

        if (INSTANCE == null){

            synchronized (TaskRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TaskRoomDatabase.class, DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    public abstract TaskDao taskDao();


}
