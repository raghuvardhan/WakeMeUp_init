package com.raghu.android.wakemeup.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.raghu.android.wakemeup.Database.AppDatabase;
import com.raghu.android.wakemeup.Database.Entities.Category;
import com.raghu.android.wakemeup.Database.Entities.SubTask;
import com.raghu.android.wakemeup.Database.Entities.Task;
import com.raghu.android.wakemeup.R;
import com.raghu.android.wakemeup.Utilities.AlarmUtilities;
import com.raghu.android.wakemeup.Utilities.AppExecutors;
import com.raghu.android.wakemeup.Utilities.Constants;
import com.raghu.android.wakemeup.Utilities.Helper;
import com.raghu.android.wakemeup.Utilities.Notifications;
import com.raghu.android.wakemeup.Utilities.Constants.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.raghu.android.wakemeup.Utilities.AlarmUtilities.TAG;
import static com.raghu.android.wakemeup.Utilities.Constants.CRITICAL_PRIORITY;
import static com.raghu.android.wakemeup.Utilities.Constants.HIGH_PRIORITY;
import static com.raghu.android.wakemeup.Utilities.Constants.LOW_PRIORITY;
import static com.raghu.android.wakemeup.Utilities.Constants.MEDIUM_PRIORITY;

public class TaskActivity extends AppCompatActivity {

    private static final String TAG = "logs";
    private Task task;

    public static final int DEFAULT_TASK_ID = -1;
    public static final String EXTRA_TASK_ID = "extraTodoId";
    public int mTaskId = DEFAULT_TASK_ID;

    private AppDatabase appDatabase;

    //Views
    private EditText taskTitleEditText;
    private EditText taskDescriptionText;
    private Spinner taskPrioritySpinner;
    private Button saveTaskBtn;
    private Button addSubTaskBtn;
    private Button taskTimeBtn;
    private TextView taskTimeText;
    private TextView taskDateText;
    private EditText taskLabelText;
    private LinearLayout subTaskLinearLayout;
    private Toolbar taskToolBar;

    private Drawable editTextBackground;

    private FloatingActionButton editFab;
    private FloatingActionButton saveFab;

    private Intent intent;
    //Menu
    private Menu menu;

    //Variables
    private int mHour;
    private int mMinute;
    private int mYear;
    private int mMonth;
    private int mDay;

    private boolean completed;

    private String taskTitle;
    private String taskDescription;

    private Time time;
    private Date date;

    private Category category;
    private int priority;

    private List<Category> categories;
    private List<SubTask> subTasks;
    private List<Integer> subTaskLayouts = new ArrayList<>();
    private List<Integer> subTaskCheckBoxes = new ArrayList<>();
    private List<Integer> subTaskEditTexts = new ArrayList<>();
    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        taskToolBar = (Toolbar) findViewById(R.id.task_toolbar);
        setSupportActionBar(taskToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        appDatabase = AppDatabase.getInstance(getApplicationContext());

        MobileAds.initialize(this, "ca-app-pub-9326529278822780~1309642359");

        mAdView = findViewById(R.id.taskAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            if (mTaskId == DEFAULT_TASK_ID) {
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        task = appDatabase.taskDao().getTaskById(mTaskId);
                        Log.e(TAG, "RecieveTaskNotification:  receive task id : " + mTaskId);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUI(task);
                            }
                        });
                    }
                });
            }
        }

        taskTitleEditText = (EditText)findViewById(R.id.task_title);
        taskDescriptionText = (EditText)findViewById(R.id.task_description);
        taskTimeText = (TextView)findViewById(R.id.task_time_text);
        taskDateText = (TextView)findViewById(R.id.task_date_text);
        taskLabelText = (EditText)findViewById(R.id.task_label_text);
        taskPrioritySpinner = (Spinner)findViewById(R.id.task_priority);
        addSubTaskBtn = (Button)findViewById(R.id.add_sub_task_button);
        saveTaskBtn = (Button)findViewById(R.id.add_sub_task_button);
        subTaskLinearLayout = (LinearLayout)findViewById(R.id.sub_task_layout);

        editFab = (FloatingActionButton)findViewById(R.id.fab_edit);
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEdit();
            }
        });

        saveFab = (FloatingActionButton)findViewById(R.id.fab_save);
        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask();
                finish();
            }
        });

        time = new Time(System.currentTimeMillis());
        date = new Date(System.currentTimeMillis());

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mMonth = c.get(Calendar.MONTH);
        mYear = c.get(Calendar.YEAR);

        //Todo get completed value from ui
        completed = false;

        taskTimeText.setText(time + "");
        taskTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(TaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                time = Helper.getTimefromTimePickerDialog(hourOfDay, minute);
                                date.setHours(hourOfDay);
                                date.setMinutes(minute);
                                taskTimeText.setText(time + "");
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        taskDateText.setText(Helper.getDateStringFromDate(date));
        taskDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(TaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                date = Helper.getDatefromDatePickerDialog(year, month, dayOfMonth);
                                Log.e(TAG, "onDateSet: date : " + Helper.getDateStringFromDate(date) );
                                taskDateText.setText(Helper.getDateStringFromDate(date));
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        taskLabelText.setText("Tag");

        final List<String> priorities = new ArrayList<String>();
        priorities.add(LOW_PRIORITY);
        priorities.add(MEDIUM_PRIORITY);
        priorities.add(HIGH_PRIORITY);
        priorities.add(CRITICAL_PRIORITY);
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text_view, priorities);
        priorityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        taskPrioritySpinner.setAdapter(priorityAdapter);
        taskPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String imp = (String)parent.getAdapter().getItem(position);
                switch(imp){
                    case "Critical" : priority = 1;
                    break;
                    case "High" : priority = 2;
                    break;
                    case "Medium" : priority = 3;
                    break;
                    case "Low" : priority = 4;
                    break;
                    default:priority = -1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        addSubTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout linearLayout = new LinearLayout(TaskActivity.this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setId(View.generateViewId());
                subTaskLayouts.add(linearLayout.getId());

                CheckBox checkBox = new CheckBox(TaskActivity.this);
                checkBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                checkBox.setChecked(false);
                checkBox.setId(View.generateViewId());
                checkBox.setTag("CheckBox");
                linearLayout.addView(checkBox);

                EditText editText = new EditText(TaskActivity.this);
                editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                editText.setHint("SubTask.....");
                editText.setId(View.generateViewId());
                editText.setTag("EditText");
                linearLayout.addView(editText);

                subTaskLinearLayout.addView(linearLayout);
            }
        });


    }

    private void populateUI(Task task) {
        disableEdit();
        if (task == null) {
            return;
        }
        taskTitleEditText.setText(task.getTitle().toString());
        taskDescriptionText.setText(task.getDescription().toString());
        taskTimeText.setText(task.getTime() + "");
        taskDateText.setText(Helper.getDateStringFromDate(task.getDate()));
        taskLabelText.setText(task.getCategory().toString());
        subTasks = appDatabase.subTaskDao().getSubTaskByOwnerId(task.getTaskId());
        for(SubTask subTask : subTasks){
            updateSubTaskLayout(subTask);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        this.menu = menu;
        inflater.inflate(R.menu.save_menu, menu);
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            showMenuItem(R.id.edit_id);
            hideMenuItem(R.id.save_id);
        }
        else{
            showMenuItem(R.id.save_id);
            hideMenuItem(R.id.edit_id);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.save_id:
                createTask();
                finish();
                return true;
            case R.id.edit_id:
                enableEdit();
                return true;
            case R.id.delete_id:
                deleteTask();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createTask(){
        getValuesFromUi();
        task = new Task(taskTitle, taskDescription, date, priority, category, completed);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(mTaskId == DEFAULT_TASK_ID){
                            mTaskId = (int) appDatabase.taskDao().insertTask(task);
                            task.setTaskId(mTaskId);
                            createSubTasks();
                            AlarmUtilities.createAlarm(TaskActivity.this, task);

                        }else{
                            task.setTaskId(mTaskId);
                            appDatabase.taskDao().updateTask(task);
                            updateSubTasks();
                            AlarmUtilities.updateAlarm(TaskActivity.this, task);
                        }
                        finish();
                    }
                });
    }

    public void deleteTask(){
        appDatabase.subTaskDao().deleteSubTaskByOwnerId(task.getTaskId());
        appDatabase.taskDao().deleteTask(task);
        AlarmUtilities.deleteAlarm(TaskActivity.this, task);
        finish();
    }

    public void createSubTasks(){
        for(Integer i : subTaskLayouts){
            LinearLayout linearLayout = (LinearLayout)findViewById(i);
            CheckBox checkBox = (CheckBox)linearLayout.findViewWithTag("CheckBox");
            EditText editText = (EditText)linearLayout.findViewWithTag("EditText");
            String subTaskMessage = editText.getText().toString();
            boolean subTaskCompleted = checkBox.isChecked();

            SubTask subTask = new SubTask(mTaskId, subTaskMessage, subTaskCompleted);
            appDatabase.subTaskDao().insertSubTask(subTask);

        }
    }

    public void updateSubTasks(){
        appDatabase.subTaskDao().deleteSubTaskByOwnerId(mTaskId);
        for(Integer i : subTaskLayouts){
            LinearLayout linearLayout = (LinearLayout)findViewById(i);
            CheckBox checkBox = (CheckBox)linearLayout.findViewWithTag("CheckBox");
            EditText editText = (EditText)linearLayout.findViewWithTag("EditText");
            String subTaskMessage = editText.getText().toString();
            boolean subTaskCompleted = checkBox.isChecked();

            SubTask subTask = new SubTask(mTaskId, subTaskMessage, subTaskCompleted);
            appDatabase.subTaskDao().insertSubTask(subTask);
        }
    }

    public void getValuesFromUi(){
        taskTitle = taskTitleEditText.getText().toString();
        taskDescription = taskDescriptionText.getText().toString();
        category = new Category(taskLabelText.getText().toString());
    }

    public void updateSubTaskLayout(SubTask subTask){
        LinearLayout linearLayout = createSubTaskLayout();
        CheckBox checkBox = (CheckBox)linearLayout.findViewWithTag("CheckBox");
        checkBox.setChecked(subTask.isCompleted());
        EditText editText = (EditText)linearLayout.findViewWithTag("EditText");
        editText.setText(subTask.getMessage());
    }

    public LinearLayout createSubTaskLayout(){

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout = new LinearLayout(TaskActivity.this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setId(View.generateViewId());
        subTaskLayouts.add(linearLayout.getId());

        CheckBox checkBox = new CheckBox(TaskActivity.this);
        checkBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        checkBox.setChecked(false);
        checkBox.setId(View.generateViewId());
        checkBox.setTag("CheckBox");
        linearLayout.addView(checkBox);

        EditText editText = new EditText(TaskActivity.this);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setHint("SubTask.....");
        editText.setId(View.generateViewId());
        editText.setTag("EditText");
        linearLayout.addView(editText);

        subTaskLinearLayout.addView(linearLayout);
        return linearLayout;
    }

    public void enableEdit(){
        taskTitleEditText.setFocusableInTouchMode(true);
        taskTitleEditText.setBackground(editTextBackground);
        taskTitleEditText.setClickable(true);

        taskDescriptionText.setFocusableInTouchMode(true);
        taskDescriptionText.setBackground(editTextBackground);
        taskDescriptionText.setClickable(true);

        showFab(R.id.fab_save);
        hideFab(R.id.fab_edit);

        showMenuItem(R.id.save_id);
        hideMenuItem(R.id.edit_id);
    }

    public void disableEdit(){
        editTextBackground = taskTitleEditText.getBackground();

        taskTitleEditText.setFocusable(false);
        taskTitleEditText.setBackground(null);
        taskTitleEditText.setClickable(false);

        taskDescriptionText.setFocusable(false);
        taskDescriptionText.setBackground(null);
        taskDescriptionText.setClickable(false);
    }

    public void hideMenuItem(int id){
         MenuItem menuItem = menu.findItem(id);
         menuItem.setVisible(false);
    }

    public void showMenuItem(int id){
        MenuItem menuItem = menu.findItem(id);
        menuItem.setVisible(true);
    }

    public void showFab(int id){
        FloatingActionButton fab = (FloatingActionButton)findViewById(id);
        fab.setVisibility(View.VISIBLE);
    }

    public void hideFab(int id){
        FloatingActionButton fab = (FloatingActionButton)findViewById(id);
        fab.setVisibility(View.INVISIBLE);
    }

    private void createLabels(Context context) {
        Intent intent = new Intent(TaskActivity.this, LabelDialogActivity.class);
        startActivity(intent);
    }

}
