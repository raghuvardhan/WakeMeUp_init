package com.raghu.android.wakemeup.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.raghu.android.wakemeup.Database.AppDatabase;
import com.raghu.android.wakemeup.Database.Entities.Task;
import com.raghu.android.wakemeup.R;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ProgressActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private int completedTasksCount;
    private int totalTasksCount;

    private TextView completedTaskText;
    private TextView totalTaskText;
    private TextView percentageText;
    private Toolbar toolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        completedTaskText = (TextView)findViewById(R.id.completed_tasks_count);
        totalTaskText = (TextView)findViewById(R.id.scheduled_tasks_count);
        percentageText = (TextView)findViewById(R.id.success_percentage);

        appDatabase  =  AppDatabase.getInstance(getApplicationContext());

        totalTasksCount = appDatabase.taskDao().getCountofTasks();

        List<Task> completedTasks = appDatabase.taskDao().getTasksByStatus(true);
        completedTasksCount = completedTasks.size();

        int percentage = 0;
        if(totalTasksCount != 0) {
            percentage = (completedTasksCount / totalTasksCount) * 100;
        }
        completedTaskText.setText(completedTasksCount + "");
        totalTaskText.setText(totalTasksCount + "");
        percentageText.setText(percentage + "");

        BarChart chart = (BarChart) findViewById(R.id.chart);

        BarData data = getDataSet();
        data.setBarWidth(1.000f);
        chart.setData(data);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        Description description = new Description();
        description.setText("Progress");
        chart.setDescription(description);
        chart.animateXY(2000, 2000);
        chart.invalidate();
    }

    private BarData getDataSet() {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(0.000f, completedTasksCount);
        valueSet1.add(v1e1);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(2.000f, totalTasksCount); // Jan
        valueSet2.add(v2e1);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Tasks Completed");
        barDataSet1.setColors(Color.rgb(255,255,0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Tasks Scheduled");
        barDataSet2.setColors(Color.rgb(0,255,0));

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        BarData barData = new BarData(barDataSet1, barDataSet2);
        return barData;
    }


    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        return xAxis;
    }
}
