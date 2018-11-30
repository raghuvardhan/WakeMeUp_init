package com.raghu.android.wakemeup.Utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.raghu.android.wakemeup.Activities.TaskActivity;
import com.raghu.android.wakemeup.Database.Entities.Task;
import com.raghu.android.wakemeup.Database.Entities.TimeTableTask;
import com.raghu.android.wakemeup.R;
import com.raghu.android.wakemeup.Receivers.CloseReceiver;
import com.raghu.android.wakemeup.Receivers.SnoozeReceiver;
import com.raghu.android.wakemeup.TimeTableFragment;

import java.sql.Time;

import static com.raghu.android.wakemeup.Activities.TaskActivity.EXTRA_TASK_ID;

public final class Notifications {

    public static void sendTaskNotification(Context context, Task task){

        String notificationTitle = task.getTitle();
        String notificationText = task.getDescription();
        int taskId = task.getTaskId();

        Log.e("logs", "sendTaskNotification: taskId : " + taskId );
        Bundle bundle = new Bundle();
        bundle.putSerializable("Task", task);

        //Start Activity on notification click
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra(EXTRA_TASK_ID, task.getTaskId());
        intent.putExtra("Bundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Snooze action
        Intent snoozeIntent = new Intent(context, SnoozeReceiver.class);
        snoozeIntent.putExtra("Bundle", bundle);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, taskId, snoozeIntent, 0);

        //Complete action
        Intent completeIntent = new Intent(context, CloseReceiver.class);
        completeIntent.putExtra("Bundle", bundle);
        PendingIntent completedPendingIntent = PendingIntent.getBroadcast(context, taskId, completeIntent, 0);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ringtone = sharedPreferences.getString("notifications_new_message_ringtone", null);

        Uri uri = Uri.parse(ringtone);

        NotificationManager notificationManager  = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.ic_snooze)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_snooze, context.getString(R.string.snooze), snoozePendingIntent)
                .addAction(R.drawable.ic_close, context.getString(R.string.completed), completedPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(uri)
                .setAutoCancel(true);

        Notification notification = notifyBuilder.build();
        notificationManager.notify(taskId, notification);

    }

    public static void sendTimeTableTaskNotification(Context context, TimeTableTask timeTableTask){

        String notificationTitle = timeTableTask.getTimeTableTask();
        int timeTableTaskId = timeTableTask.getTimeTableTaskId();


        //Start Activity on notification click
        Intent intent = new Intent(context, TimeTableFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, timeTableTaskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ringtone = sharedPreferences.getString("notifications_new_message_ringtone", null);

        Uri uri = Uri.parse(ringtone);

        NotificationManager notificationManager  = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(notificationTitle)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(uri)
                .setAutoCancel(true);

        Notification notification = notifyBuilder.build();
        notificationManager.notify(timeTableTaskId, notification);
    }

    public static void clearNotification(Context context, int notificationId){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }

}
