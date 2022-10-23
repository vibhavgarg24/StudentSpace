package com.example.studentspace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int NOTIFICATION_ID = intent.getExtras().getInt("NOTIFICATION_ID");
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder builder = notificationHelper.getNotification();
        notificationHelper.getManager().notify(NOTIFICATION_ID, builder.build());

    }
}
