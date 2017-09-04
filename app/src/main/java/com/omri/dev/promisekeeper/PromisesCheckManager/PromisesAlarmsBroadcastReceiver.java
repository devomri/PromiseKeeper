package com.omri.dev.promisekeeper.PromisesCheckManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.omri.dev.promisekeeper.AboutActivity;
import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.Model.PromiseVerifier;
import com.omri.dev.promisekeeper.R;

public class PromisesAlarmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PromiseListItem promise = new PromiseListItem(intent);
        PromiseVerifier verifier = new PromiseVerifier(context);
        verifier.verifyPromise(promise);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, AboutActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification n = new Notification.Builder(context)
                .setContentTitle("New promise arrived: " + promise.getmTitle())
                .setContentText(promise.getmDescription())
                .setSmallIcon(R.drawable.ic_done_white)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .build();

        // TODO: Check if promise is kept

        notificationManager.notify(1, n);
    }
}
