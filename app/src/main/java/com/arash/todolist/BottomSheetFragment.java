package com.arash.todolist;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arash.todolist.model.Priority;
import com.arash.todolist.model.SharedViewModel;
import com.arash.todolist.model.Task;
import com.arash.todolist.model.TaskViewModel;
import com.arash.todolist.util.Converter;
import com.arash.todolist.util.Util;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;
//import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private EditText enterTodo;
    private ImageButton calendarButton;
    private ImageButton priorityButton;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioButton;
    private int selectedButtonId;
    private ImageButton saveButton;
    private CalendarView calendarView;
    private Group calendarGroup;
    private Date dueDate;
    Calendar calendar = Calendar.getInstance();

    private View v;
    private SharedViewModel sharedViewModel;

    private boolean isEdit;

    private Priority priority;

    private boolean isNew;


    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);


        calendarGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);
        calendarButton = view.findViewById(R.id.today_calendar_button);
        enterTodo = view.findViewById(R.id.enter_todo_et);
        saveButton = view.findViewById(R.id.save_todo_button);
        priorityButton = view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);


        enterTodo.setText("");

        Chip todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
        nextWeekChip.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sharedViewModel.getSelectedItem().getValue() != null){
            Task task = sharedViewModel.getSelectedItem().getValue();

            enterTodo.setText(task.getTask());

            isEdit = sharedViewModel.getIsEdit();


        }
        isNew = sharedViewModel.getIsNew();

        if(isNew){
            enterTodo.setText("");
            dueDate = Calendar.getInstance().getTime();

            priority = Priority.NONE;
            sharedViewModel.setIsNew(false);
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarGroup.setVisibility(calendarGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

                Util.hideSoftKeyboard(view);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                // i = year , i1 = month , i2 = day of month

                dueDate = calendar.getTime();
                calendar.clear();
                calendar.set(i,i1,i2);
                dueDate = calendar.getTime();
            }
        });

        priorityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                Util.hideSoftKeyboard(view1);
                priorityRadioGroup.setVisibility(
                        priorityRadioGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE
                );
                priorityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if(priorityRadioGroup.getVisibility() == View.VISIBLE){
                            selectedButtonId = i;
                            selectedRadioButton = view.findViewById(selectedButtonId);
                            if (selectedRadioButton.getId() == R.id.radioButton_high){
                                priority = Priority.HIGH;
                            }else if (selectedRadioButton.getId() == R.id.radioButton_med){
                                priority = Priority.MEDIUM;
                            }else if (selectedRadioButton.getId() == R.id.radioButton_low){
                                priority = Priority.LOW;
                            }else{
                                priority = Priority.LOW;
                            }
                        }
                    }
                });
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = enterTodo.getText().toString().trim();
                if (!TextUtils.isEmpty(task) && dueDate != null){
                    Task myTask = new Task(task, priority, dueDate,
                            Calendar.getInstance().getTime(), false,0);


                    if ( isEdit == true){
                        Task updateTask = sharedViewModel.getSelectedItem().getValue();

                        updateTask.setTask(task);
                        updateTask.setDateCreated(Calendar.getInstance().getTime());
                        updateTask.setPriority(priority);
                        updateTask.setDueDate(dueDate);
                        updateTask.setDone(updateTask.isDone());
                        TaskViewModel.update(updateTask);

                        sharedViewModel.setIsEdit(false);

                    }else {
                        TaskViewModel.insert(myTask);
                    }
                    enterTodo.setText("");
                    if (BottomSheetFragment.this.isVisible()){
                        BottomSheetFragment.this.dismiss();
                    }
                }else{
                    String notif = "";
                    if(TextUtils.isEmpty(task))
                        notif = "Enter Text";
                    else if (dueDate == null)
                        notif = "Enter Date";

                    Toast.makeText(getContext() , notif ,Toast.LENGTH_SHORT).show();
                    Snackbar.make(saveButton, notif,Snackbar.LENGTH_SHORT).show();
                }

            }

        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id==R.id.today_chip){
            calendar = Calendar.getInstance();
            dueDate = (calendar.getTime());
        }else if (id==R.id.tomorrow_chip){
            calendar = Calendar.getInstance();

            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dueDate = calendar.getTime();
        }else if (id==R.id.next_week_chip){
            calendar = Calendar.getInstance();

            calendar.add(Calendar.DAY_OF_YEAR, 7);
            dueDate = calendar.getTime();
        }

    }
}