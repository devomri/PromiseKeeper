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
 * Created by omri on 9/2/17.
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
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                promiseTime.getTimeInMillis(),
                pi);
    }
}
