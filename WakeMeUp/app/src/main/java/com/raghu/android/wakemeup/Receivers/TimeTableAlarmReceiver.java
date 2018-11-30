package com.raghu.android.wakemeup.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.raghu.android.wakemeup.Database.Entities.TimeTableTask;
import com.raghu.android.wakemeup.Utilities.Notifications;

public class TimeTableAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final TimeTableTask timeTableTask = (TimeTableTask) intent.getExtras().getBundle("Bundle").getSerializable("TimeTableTask");
        Notifications.sendTimeTableTaskNotification(context, timeTableTask);
    }
}
