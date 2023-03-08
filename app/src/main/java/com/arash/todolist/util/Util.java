package com.arash.todolist.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.arash.todolist.model.Priority;
import com.arash.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static String formatDare (Date date){

        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("EEE, MMM d");

        return simpleDateFormat.format(date);
    }
    public static void hideSoftKeyboard (View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int priorityColor(Task task){
        int color;
        if (task.getPriority() == Priority.HIGH){
            color = Color.argb(200, 255, 43, 0);
        }else if (task.getPriority() == Priority.MEDIUM){
            color = Color.argb(200, 255, 157, 0);
        }else if (task.getPriority() == Priority.LOW){
            color = Color.argb(200, 52, 204, 255);
        }else{
            color = Color.argb(200, 96, 96, 96);
        }
        return color;
    }
}
