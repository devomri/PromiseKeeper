package com.omri.dev.promisekeeper.PromisesCheckManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.omri.dev.promisekeeper.AboutActivity;
import com.omri.dev.promisekeeper.Model.PromiseListItem;
import com.omri.dev.promisekeeper.Utils.DateUtils;

import java.util.Calendar;

/**
 * This class schedules the future promise verification times
 */
public class PromisesAlarmsShooter {
    private AlarmManager mAlarmManager;
    private Context mContext;

    public PromisesAlarmsShooter(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
    }

    public void createAnAlarmForPromise(PromiseListItem promise) {
        Calendar promiseTime = DateUtils.convertStringToCalendar(promise.getmBaseTime());

        Intent i = promise.toIntent(mContext, PromisesAlarmsBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, (int) System.currentTimeMillis(), i, PendingIntent.FLAG_ONE_SHOT);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                promiseTime.getTimeInMillis(),
                pi);
    }
}
