package com.raghu.android.wakemeup.Utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.raghu.android.wakemeup.Database.Entities.Alarm;
import com.raghu.android.wakemeup.Database.AppDatabase;
import com.raghu.android.wakemeup.Database.Entities.Task;
import com.raghu.android.wakemeup.Database.Entities.TimeTableTask;
import com.raghu.android.wakemeup.Database.typeConverters.DateConverter;
import com.raghu.android.wakemeup.Database.typeConverters.TimeConverter;
import com.raghu.android.wakemeup.Receivers.AlarmReceiver;
import com.raghu.android.wakemeup.Receivers.SnoozeReceiver;
import com.raghu.android.wakemeup.Receivers.TimeTableAlarmReceiver;


import java.net.Inet4Address;
import java.sql.Date;
import java.sql.Time;

import static android.content.Context.ALARM_SERVICE;

public final class AlarmUtilities {

    public static final String TAG = "Logs";
    private static AppDatabase appDatabase;

    public static void createAlarm(Context context, Task task){

        long timeToAlarm = task.getDate().getTime();

        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Task", task);
        intent.putExtra("Bundle", bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getTaskId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeToAlarm, pendingIntent);

    }

    public static void updateAlarm(Context context, Task task){
        createAlarm(context, task);
    }

    public static void deleteAlarm(Context context, Task task){
        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Task", task);
        intent.putExtra("Bundle", bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getTaskId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void snoozeAlarm(Context context, Task task){
        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Task", task);
        intent.putExtra("Bundle", bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getTaskId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, pendingIntent);
    }

    public static void createTimeTableAlarm(Context context, TimeTableTask timeTableTask){
        Intent intent = new Intent(context, TimeTableAlarmReceiver.class);
        long timeToAlarm = timeTableTask.getStartTime().getTime();
        Bundle bundle = new Bundle();
        bundle.putSerializable("TimeTableTask", timeTableTask);
        intent.putExtra("Bundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, timeTableTask.getTimeTableTaskId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeToAlarm,AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static void updateTimeTableAlarm(Context context, TimeTableTask timeTableTask){
        createTimeTableAlarm(context, timeTableTask);
    }

    public static void deleteTimeTableAlarm(Context context, TimeTableTask timeTableTask){
        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("TimeTableTask", timeTableTask);
        intent.putExtra("Bundle", bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, timeTableTask.getTimeTableTaskId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}