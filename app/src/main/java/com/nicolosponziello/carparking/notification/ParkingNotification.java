package com.nicolosponziello.carparking.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.nicolosponziello.carparking.MainActivity;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.util.Const;

/**
 * classe per la creazione delle notifiche
 */
public class ParkingNotification {

    private NotificationChannel channel;

    /**
     * inizializza un channel per l'invio delle notifiche
     * @param context contesto
     */
    public static void createChannel(Context context){
        //esegui la funzione solo se le api sono 26+, sotto non esiste il channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = context.getString(R.string.notif_channel_name);
            String desc = context.getString(R.string.notif_channel_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Const.NOTIF_CHANNEL_ID, name, importance);
            channel.setDescription(desc);

            //registra il channel
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    /**
     * istanzia una nuova notifica utilizzando il builder
     * @param context
     * @return
     */
    public static Notification createExpirationNotification(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Const.NOTIF_CHANNEL_ID)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_text))
            .setSmallIcon(R.drawable.parking_tile_icon)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder.build();
    }
}
