package com.nicolosponziello.carparking.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.util.Const;

public class ParkingNotification {

    private NotificationChannel channel;

    public static void createChannel(Context context){
        /* check if device has api26+  because the notification channel is not on below apis*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = context.getString(R.string.notif_channel_name);
            String desc = context.getString(R.string.notif_channel_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Const.NOTIF_CHANNEL_ID, name, importance);
            channel.setDescription(desc);

            //register the channel
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public static Notification createExpirationNotification(Context context){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Const.NOTIF_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_skip_white)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_text))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder.build();
    }
}
