package com.nicolosponziello.carparking.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.nicolosponziello.carparking.notification.NotifManager;
import com.nicolosponziello.carparking.notification.ParkingNotification;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int NOTFICATION_ID = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        //show the notification
        Log.d("Alarm", "receiver");
        Notification notification = ParkingNotification.createExpirationNotification(context);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(NOTFICATION_ID, notification);
        NotifManager.getInstance().stopAlarm();
    }
}
