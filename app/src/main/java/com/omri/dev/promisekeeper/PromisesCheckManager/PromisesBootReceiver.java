package com.omri.dev.promisekeeper.PromisesCheckManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.omri.dev.promisekeeper.AboutActivity;
import com.omri.dev.promisekeeper.DAL.PromisesDAL;
import com.omri.dev.promisekeeper.Model.PromiseEnumConvrsions;
import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.R;
import com.omri.dev.promisekeeper.Utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class intended to run when the devices is booted
 * In order to reschedule the future promises
 * (Because alarmManager cannot save tasks after a reboot)
 */
public class PromisesBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            PromisesDAL promisesDAL = new PromisesDAL(context);
            PromisesAlarmsShooter shooter = new PromisesAlarmsShooter(context);

            ArrayList<PromiseListItem> futurePromises = promisesDAL.getFuturePromises();

            Date now = new Date();
            for (PromiseListItem promise : futurePromises) {
                Date promiseDate = DateUtils.convertStringToDate(promise.getmBaseTime());
                if (promiseDate.before(now)) {
                    promisesDAL.markPromiseAsUnfulfilled(promise.getmPromiseID());
                } else {
                    shooter.createAnAlarmForPromise(promise);
                }
            }
        }
    }
}
