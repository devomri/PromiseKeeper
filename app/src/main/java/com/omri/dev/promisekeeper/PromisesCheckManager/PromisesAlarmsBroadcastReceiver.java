package com.omri.dev.promisekeeper.PromisesCheckManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.omri.dev.promisekeeper.AboutActivity;
import com.omri.dev.promisekeeper.AskGeneralPromiseFulfillment;
import com.omri.dev.promisekeeper.DAL.PromisesDAL;
import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.Model.PromiseStatus;
import com.omri.dev.promisekeeper.Model.PromiseTypes;
import com.omri.dev.promisekeeper.Model.PromiseVerifier;
import com.omri.dev.promisekeeper.PromiseDetailsActivity;
import com.omri.dev.promisekeeper.R;

public class PromisesAlarmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        final PromisesDAL dal = new PromisesDAL(context);

        final PromiseListItem promise = new PromiseListItem(intent);
        final NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Cannot verify
        if (promise.getmPromiseType() == PromiseTypes.GENERAL) {
            // Ask the user for verification
            Intent askGeneralPromise = promise.toIntent(context, AskGeneralPromiseFulfillment.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, askGeneralPromise, PendingIntent.FLAG_ONE_SHOT);
            Notification n = new Notification.Builder(context)
                    .setContentTitle("Did you keep '" + promise.getmTitle() + "'?")
                    .setContentText(promise.getmDescription())
                    .setSmallIcon(R.drawable.ic_promise_notification)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .build();

            notificationManager.notify(1, n);

        } else {
            PromiseVerifier verifier = new PromiseVerifier(context);
            verifier.verifyPromise(promise, new PromiseVerifier.PromiseVerificationResult() {
                @Override
                public void onVerificationFinished(boolean isPromiseKept) {
                    String message = "Promise '" + promise.getmTitle() + "' ";
                    if (isPromiseKept) {
                        message += "is kept!";
                        promise.setmPromiseStatus(PromiseStatus.FULFILLED);
                        dal.markPromisefulfilled(promise.getmPromiseID());
                    } else {
                        message += "was not kept";
                        promise.setmPromiseStatus(PromiseStatus.UNFULFILLED);
                        dal.markPromiseAsUnfulfilled(promise.getmPromiseID());

                        // If there is a guard, notify him
                        promise.sendUnfulfilledPromiseToGuard();
                    }

                    dal.createNextPromiseIfNecessary(promise);

                    Intent notificationIntent = promise.toIntent(context, PromiseDetailsActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                    Notification n = new Notification.Builder(context)
                            .setContentTitle(message)
                            .setContentText(promise.getmDescription())
                            .setSmallIcon(R.drawable.ic_promise_notification)
                            .setAutoCancel(true)
                            .setContentIntent(pi)
                            .build();

                    notificationManager.notify(1, n);

                }
            });
        }
    }
}
