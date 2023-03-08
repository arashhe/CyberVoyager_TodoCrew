package com.arash.todolist.adapter;

import com.arash.todolist.model.Task;
public interface OnToDoClickListener {
    void onTodoClick(Task task);
    void onTodoRadioButtonClick(Task task);
    void onNewClickListener(Task task);
}
