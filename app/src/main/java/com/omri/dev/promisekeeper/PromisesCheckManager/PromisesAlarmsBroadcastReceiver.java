package com.omri.dev.promisekeeper.PromisesCheckManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.omri.dev.promisekeeper.AboutActivity;
import com.omri.dev.promisekeeper.R;

public class PromisesAlarmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Intent i = new Intent(context, AboutActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        Notification n = new Notification.Builder(context)
                .setContentTitle("New promise arrived")
                .setContentText("This is the text")
                .setSmallIcon(R.drawable.ic_done_white)
                .setContentIntent(pi)
                .build();

        notificationManager.notify(1, n);
    }
}
