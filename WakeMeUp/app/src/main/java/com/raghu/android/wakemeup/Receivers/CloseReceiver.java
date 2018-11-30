package com.raghu.android.wakemeup.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.raghu.android.wakemeup.Database.AppDatabase;
import com.raghu.android.wakemeup.Database.Entities.Alarm;
import com.raghu.android.wakemeup.Database.Entities.Task;
import com.raghu.android.wakemeup.Utilities.Notifications;

public class CloseReceiver extends BroadcastReceiver {

    private AppDatabase appDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {

        Task task = (Task)  intent.getBundleExtra("Bundle").getSerializable("Task");

        task.setCompleted(true);

        appDatabase = AppDatabase.getInstance(context);
        appDatabase.taskDao().updateTask(task);

        Notifications.clearNotification(context, task.getTaskId());
    }
}
