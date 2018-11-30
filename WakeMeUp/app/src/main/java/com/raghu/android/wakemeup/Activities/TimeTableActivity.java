package com.raghu.android.wakemeup.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.raghu.android.wakemeup.Database.AppDatabase;
import com.raghu.android.wakemeup.Database.Entities.TimeTableTask;
import com.raghu.android.wakemeup.R;
import com.raghu.android.wakemeup.Utilities.AppExecutors;
import com.raghu.android.wakemeup.Utilities.Helper;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class TimeTableActivity extends AppCompatActivity {

    public static final int DEFAULT_TIMETABLE_TASK_ID = -1;
    public static final String EXTRA_TASK_ID = "extraTodoId";
    public int mTimeTableTaskId = DEFAULT_TIMETABLE_TASK_ID;

    private AppDatabase appDatabase;

    private EditText timeTableTaskText;
    private EditText timeTableStartTime;
    private EditText timeTableEndTime;
    private Button timeTableSaveBtn;

    private String timeTableTaskMsg;
    private Date timeTableTaskStartTime;
    private Date timeTableTaskEndTime;

    private Toolbar taskToolBar;

    //Variables
    private int mHour;
    private int mMinute;
    private int mYear;
    private int mMonth;
    private int mDay;

    private Time time;
    private Date date;
    private AdView mAdView;

    private Intent intent;


    private TimeTableTask timeTableTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_dialog);

        taskToolBar = (Toolbar) findViewById(R.id.timeTable_toolbar);
        setSupportActionBar(taskToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        appDatabase = AppDatabase.getInstance(getApplicationContext());

        MobileAds.initialize(this, "ca-app-pub-9326529278822780~1309642359");

        mAdView = findViewById(R.id.timeTableAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        timeTableTaskText = (EditText)findViewById(R.id.timetable_task_text);
        timeTableStartTime = (EditText)findViewById(R.id.task_start_time_text);
        timeTableEndTime = (EditText)findViewById(R.id.task_end_time_text);

        time = new Time(System.currentTimeMillis());
        date = new Date(System.currentTimeMillis());

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mMonth = c.get(Calendar.MONTH);
        mYear = c.get(Calendar.YEAR);


        intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            if (mTimeTableTaskId == DEFAULT_TIMETABLE_TASK_ID) {
                mTimeTableTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TIMETABLE_TASK_ID);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        timeTableTask = appDatabase.timeTableTaskDao().getTimeTableTaskById(mTimeTableTaskId);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUI(timeTableTask);
                            }
                        });
                    }
                });
            }
        } else{
            timeTableTaskMsg = timeTableTaskText.getText().toString();
            timeTableTaskStartTime = new Date(System.currentTimeMillis());
            timeTableTaskEndTime = new Date(System.currentTimeMillis());
        }

        timeTableStartTime.setText(time + "");
        timeTableStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(TimeTableActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                time = Helper.getTimefromTimePickerDialog(hourOfDay, minute);
                                timeTableTaskStartTime = new Date(time.getTime());
                                timeTableStartTime.setText(time + "");
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        timeTableEndTime.setText(time + "");
        timeTableEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(TimeTableActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                time = Helper.getTimefromTimePickerDialog(hourOfDay, minute);
                                timeTableTaskEndTime = new Date(time.getTime());
                                timeTableEndTime.setText(time + "");
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        timeTableSaveBtn = (Button)findViewById(R.id.timetable_task_save_btn);
        timeTableSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimeTableTask();
            }
        });
    }

    public void createTimeTableTask(){
         timeTableTaskMsg = timeTableTaskText.getText().toString();

         timeTableTask = new TimeTableTask(timeTableTaskMsg, timeTableTaskStartTime, timeTableTaskEndTime);

         AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if(mTimeTableTaskId == DEFAULT_TIMETABLE_TASK_ID){
                        mTimeTableTaskId = (int) appDatabase.timeTableTaskDao().insertTimeTableTask(timeTableTask);
                        timeTableTask.setTimeTableTaskId(mTimeTableTaskId);
                    }else{
                        timeTableTask.setTimeTableTaskId(mTimeTableTaskId);
                        appDatabase.timeTableTaskDao().updateTimeTableTask(timeTableTask);
                    }
                    finish();
                }
         });

    }

    private void populateUI(TimeTableTask timeTableTask) {
        if (timeTableTask == null) {
            return;
        }
        timeTableTaskStartTime = timeTableTask.getStartTime();
        timeTableTaskEndTime = timeTableTask.getEndTime();

        timeTableTaskText.setText(timeTableTask.getTimeTableTask());
        timeTableStartTime.setText(Helper.timeFormatter(timeTableTask.getStartTime()));
        timeTableEndTime.setText(Helper.timeFormatter(timeTableTask.getEndTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
