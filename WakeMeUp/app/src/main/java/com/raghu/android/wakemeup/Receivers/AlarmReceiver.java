package com.raghu.android.wakemeup.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.raghu.android.wakemeup.Database.Entities.Task;
import com.raghu.android.wakemeup.Utilities.Notifications;



public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final Task task = (Task) intent.getExtras().getBundle("Bundle").getSerializable("Task");
        Notifications.sendTaskNotification(context, task);
    }
}
