package com.nicolosponziello.carparking.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.nicolosponziello.carparking.receivers.AlarmReceiver;
import com.nicolosponziello.carparking.util.Const;

public class NotifManager {

    private static NotifManager notifManager;
    private static PendingIntent pendingIntent;
    private static AlarmManager manager;

    private NotifManager(){

    }

    public static NotifManager getInstance(){
        if(notifManager == null){
            notifManager = new NotifManager();
        }
        return notifManager;
    }

    public void setupAlarm(AlarmManager alarmManager, long time, Context context){
        Intent intent = new Intent(context, AlarmReceiver.class);
        manager = alarmManager;
        pendingIntent = PendingIntent.getBroadcast(context, Const.REQUEST_ALARM, intent, 0);
        alarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    public void stopAlarm(){
        manager.cancel(pendingIntent);
    }
}
