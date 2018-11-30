package com.raghu.android.wakemeup.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.raghu.android.wakemeup.Database.Entities.Task;
import com.raghu.android.wakemeup.Utilities.AlarmUtilities;

public class SnoozeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //AlarmMedia.getInstance(context).stopAlarmMedia();
        final Task task = (Task) intent.getExtras().getBundle("Bundle").getSerializable("Task");
        AlarmUtilities.snoozeAlarm(context, task);
    }
}
