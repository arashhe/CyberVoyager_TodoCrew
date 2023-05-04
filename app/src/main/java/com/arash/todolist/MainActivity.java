package com.arash.todolist;


import android.content.Intent;
import android.os.Bundle;

import com.arash.todolist.adapter.OnToDoClickListener;
import com.arash.todolist.adapter.RecyclerViewAdapter;
import com.arash.todolist.model.SharedViewModel;
import com.arash.todolist.model.Task;
import com.arash.todolist.model.TaskViewModel;
import com.arash.todolist.util.Converter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements OnToDoClickListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private TaskViewModel taskViewModel;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    BottomSheetFragment bottomSheetFragment;

    private SharedViewModel sharedViewModel;

    private RelativeLayout relativeLayout;

    private ImageButton allTasksButton, todayTasksButton, tomorrowTasksButton, doneTasksButton;
//
//    private Date date;
//    private  long longDate;
//    private String stringLongDate;

    private Calendar calendarToConcvert;
    private Date todayAmDate, tomorrowAmDate, theDayAfterTomorrowAmDate;

    //private String searchSTR;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.my_navigation_menu);
        navigationView.setNavigationItemSelectedListener(this);

//        new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                if (id == R.id.menu_done_tasks){
//                    onClick(doneTasksButton);
//                }
//                return false;
//            }
//        }

        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        allTasksButton = findViewById(R.id.all_tasks);
        todayTasksButton = findViewById(R.id.today_tasks);
        tomorrowTasksButton = findViewById(R.id.tomorrow_tasks);
        doneTasksButton = findViewById(R.id.done_tasks);

        allTasksButton.setOnClickListener(this);
        todayTasksButton.setOnClickListener(this);
        tomorrowTasksButton.setOnClickListener(this);
        doneTasksButton.setOnClickListener(this);

        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this.getApplication())
                .create(TaskViewModel.class);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);


        onClick(allTasksButton);
        Log.i("TAG", "onCreate: ");

        //date = Calendar.getInstance().getTime();
        //longDate = Converter.dateToTimestamp(date);
        //stringLongDate = String.valueOf(longDate);
        //stringLongDate = "16778989%";
        //Log.i("TAGA", "onResume: RESUME "+ stringLongDate);


        calendarToConcvert = Calendar.getInstance();
        calendarToConcvert.set(Calendar.HOUR_OF_DAY, 0);
        calendarToConcvert.set(Calendar.MINUTE, 0);
        calendarToConcvert.set(Calendar.SECOND, 0);
        calendarToConcvert.set(Calendar.MILLISECOND, 0);
        todayAmDate = calendarToConcvert.getTime();
        //longDate = Converter.dateToTimestamp(todayAmDate);

        calendarToConcvert = Calendar.getInstance();
        calendarToConcvert.add(Calendar.DAY_OF_YEAR, 1);
        calendarToConcvert.set(Calendar.HOUR_OF_DAY, 0);
        calendarToConcvert.set(Calendar.MINUTE, 0);
        calendarToConcvert.set(Calendar.SECOND, 0);
        calendarToConcvert.set(Calendar.MILLISECOND, 0);
        tomorrowAmDate = calendarToConcvert.getTime();

        calendarToConcvert = Calendar.getInstance();
        calendarToConcvert.add(Calendar.DAY_OF_YEAR, 2);
        calendarToConcvert.set(Calendar.HOUR_OF_DAY, 0);
        calendarToConcvert.set(Calendar.MINUTE, 0);
        calendarToConcvert.set(Calendar.SECOND, 0);
        calendarToConcvert.set(Calendar.MILLISECOND, 0);
        theDayAfterTomorrowAmDate = calendarToConcvert.getTime();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Task task = new Task("Todo", Priority.HIGH, Calendar.getInstance().getTime(),
//                        Calendar.getInstance().getTime(),false,0);
//
//                TaskViewModel.insert(task);

                sharedViewModel.setIsNew(true);
                //Log.i("TAG", "onResume: RESUME "+ sharedViewModel.getIsNew());
                showBottomSheetDialog();
            }
        });
    }



    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        int id2 = item.;
        Log.d("TEST", "onOptionsItemSelected: before1");

        if (id == R.id.done_tasks) {
            Log.d("TEST", "onOptionsItemSelected: after");
            onClick(doneTasksButton);
            startActivity(new Intent(this, AboutActivity.class));

//            return true;
        }

        Log.d("TEST", "onOptionsItemSelected: before2");

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        Log.d("TEST", "onOptionsItemSelected: before3");


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, AboutActivity.class));

//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTodoClick(Task task) {

        sharedViewModel.selectItem(task);

        sharedViewModel.setIsEdit(true);

        showBottomSheetDialog();
    }

    @Override
    public void onTodoRadioButtonClick(Task task) {

        TaskViewModel.delete(task);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNewClickListener(Task task) {
        sharedViewModel.setIsNew(true);
    }

    LiveData<List<Task>> getAllTasks = null;
    LiveData<List<Task>> today = null;
    LiveData<List<Task>> tomorrow = null;
    LiveData<List<Task>> doneTask = null;

    Observer<List<Task>> dataChangeObserver = new DataChangeObserver();

    void removeAll() {
        if (getAllTasks != null) {
            getAllTasks.removeObservers(this);
        }
        if (today != null) {
            today.removeObservers(this);
        }
        if (tomorrow != null) {
            tomorrow.removeObservers(this);
        }
        if (doneTask != null) {
            doneTask.removeObservers(this);
        }
    }

    @Override
    public void onBackPressed() {
        drawerLayout.closeDrawers();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        removeAll();
        if (id == R.id.all_tasks) {
            getAllTasks = taskViewModel.getAllTasks();
            getAllTasks.observe(this, dataChangeObserver);
        } else if (id == R.id.today_tasks) {
            today = taskViewModel.getSpecificData(String.valueOf(Converter.dateToTimestamp(todayAmDate)), String.valueOf(Converter.dateToTimestamp(tomorrowAmDate)));
            today.observe(this, dataChangeObserver);
        } else if (id == R.id.tomorrow_tasks) {
            tomorrow = taskViewModel.getSpecificData(String.valueOf(Converter.dateToTimestamp(tomorrowAmDate)), String.valueOf(Converter.dateToTimestamp(theDayAfterTomorrowAmDate)));
            tomorrow.observe(this, dataChangeObserver);
        } else if (id == R.id.done_tasks) {
            doneTask = taskViewModel.getDoneTasks("1");
            doneTask.observe(this, dataChangeObserver);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        removeAll();
        if (id == R.id.menu_all_tasks) {
            getAllTasks = taskViewModel.getAllTasks();
            getAllTasks.observe(this, dataChangeObserver);
        } else if (id == R.id.menu_today_tasks) {
            today = taskViewModel.getSpecificData(String.valueOf(Converter.dateToTimestamp(todayAmDate)), String.valueOf(Converter.dateToTimestamp(tomorrowAmDate)));
            today.observe(this, dataChangeObserver);
        } else if (id == R.id.menu_tomorrow_tasks) {
            tomorrow = taskViewModel.getSpecificData(String.valueOf(Converter.dateToTimestamp(tomorrowAmDate)), String.valueOf(Converter.dateToTimestamp(theDayAfterTomorrowAmDate)));
            tomorrow.observe(this, dataChangeObserver);
        } else if (id == R.id.menu_done_tasks) {
            doneTask = taskViewModel.getDoneTasks("1");
            doneTask.observe(this, dataChangeObserver);
        }

        drawerLayout.closeDrawers();
        return false;
    }

    class DataChangeObserver implements Observer<List<Task>> {

        @Override
        public void onChanged(List<Task> tasks) {
            recyclerViewAdapter = new RecyclerViewAdapter(tasks, MainActivity.this);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }
}


//interface MyObserver<T> {
//    void onChanged(T t);
//}
//
//class MyLiveData<T> {
//    List<MyObserver<T>> observers = new ArrayList<>();
//
//    void observe(MyObserver<T> observer){
//        observers.add(observer);
//    }
//    void add(T t){
//        for(MyObserver<T> obs : observers){
//            obs.onChanged(t);
//        }
//    }
//}