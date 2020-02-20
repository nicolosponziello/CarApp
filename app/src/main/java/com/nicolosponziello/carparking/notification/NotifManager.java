package com.nicolosponziello.carparking.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.nicolosponziello.carparking.receivers.AlarmReceiver;
import com.nicolosponziello.carparking.util.Const;

/**
 * manager per le notifiche dell'applicazione
 */
public class NotifManager {

    private static NotifManager notifManager;
    private static PendingIntent pendingIntent;
    private static AlarmManager manager;

    private NotifManager(){ }

    //singleton pattern
    public static NotifManager getInstance(){
        if(notifManager == null){
            notifManager = new NotifManager();
        }
        return notifManager;
    }

    /**
     * utilizza l'alarm manager per settare un'evento ad un determinato evento temporale
     * @param alarmManager istanza dell'alarm manager passato tramite pattern strategy
     * @param time timestamp trigger
     * @param context contesto
     */
    public void setupAlarm(AlarmManager alarmManager, long time, Context context){
        //intent per il receiver
        Intent intent = new Intent(context, AlarmReceiver.class);
        manager = alarmManager;
        //utilizza un pending intent, che verrà risolto in futuro
        pendingIntent = PendingIntent.getBroadcast(context, Const.REQUEST_ALARM, intent, 0);
        alarmManager.setExact(android.app.AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    /**
     * ferma l'alarm manager settato corrententemente
     */
    public void stopAlarm(){
        if(pendingIntent != null) {
            manager.cancel(pendingIntent);
        }
    }
}
