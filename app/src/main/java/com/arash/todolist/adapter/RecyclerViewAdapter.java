package com.arash.todolist.adapter;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.arash.todolist.R;
import com.arash.todolist.model.SharedViewModel;
import com.arash.todolist.model.Task;
import com.arash.todolist.model.TaskViewModel;
import com.arash.todolist.util.Util;
import com.google.android.material.chip.Chip;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final List<Task> taskList;

    private final OnToDoClickListener toDoClickListener;

    private SharedViewModel sharedViewModel;

    public RecyclerViewAdapter(List<Task> taskList , OnToDoClickListener onToDoClickListener) {
        this.taskList = taskList;
        this.toDoClickListener = onToDoClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Task task = taskList.get(position);
        String formatted = Util.formatDare(task.getDueDate());

        ColorStateList colorStateList = new ColorStateList(new int[][]{
                new int[] {-android.R.attr.state_enabled},
                new int[] {android.R.attr.state_enabled}
        } , new int[]{
                Color.LTGRAY ,
                Util.priorityColor(task)
        });

        holder.task.setText(task.getTask());
        holder.todayChip.setText(formatted);
        holder.todayChip.setTextColor(Util.priorityColor(task));
        holder.todayChip.setChipIconTint(colorStateList);
        holder.radioButton.setButtonTintList(colorStateList);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder  extends  RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener{
        public AppCompatRadioButton radioButton ;
        public AppCompatTextView task;
        public Chip todayChip;

        OnToDoClickListener onToDoClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            radioButton = itemView.findViewById(R.id.todo_radio_button);
            task = itemView.findViewById(R.id.todo_row_todo);
            todayChip = itemView.findViewById(R.id.todo_row_chip);
            todayChip = itemView.findViewById(R.id.todo_row_chip);

            task.setOnLongClickListener(this) ;

            task.setOnClickListener(this);

            this.onToDoClickListener = toDoClickListener;

            itemView.setOnClickListener(this);

            radioButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Task currTask = taskList.get(getAdapterPosition());
            int id = view.getId();
            if (id == R.id.todo_row_layout){
                onToDoClickListener.onTodoClick(currTask);
            }else if ( id == R.id.todo_radio_button){
                //onToDoClickListener.onTodoRadioButtonClick(currTask);

                currTask.setTask(currTask.getTask());
                currTask.setDateCreated(currTask.getDateCreated());
                currTask.setPriority(currTask.getPriority());
                currTask.setDueDate(currTask.getDueDate());
//                currTask.setDone(true);
                if(currTask.isDone()) {
//                    Log.i("TASKDONE= ", "TRUE");
                    currTask.setDone(false);
                }else{
//                    Log.i("TASKDONE= ", "FALSE");
                    currTask.setDone(true);
                }
                TaskViewModel.update(currTask);
            }else if ( id == R.id.todo_row_todo){
                onToDoClickListener.onTodoClick(currTask);
            }
        }

        @Override
        public boolean onLongClick(View view) {

            final Dialog dialog = new Dialog(view.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.delete_pop_up_window);

            Button yes = dialog.findViewById(R.id.yes_button);
            Button no = dialog.findViewById(R.id.no_button);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onToDoClickListener.onTodoRadioButtonClick(taskList.get(getAdapterPosition()));
                    dialog.dismiss();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();



            return false;
        }
    }
}

