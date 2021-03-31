package com.example.broadcastreceiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

public class TimeChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "Test Id";
        String channelName = "Test Channel";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Alert");
        builder.setContentText("Device Time Channed");

        Intent in = new Intent(Settings.ACTION_DATE_SETTINGS);
        PendingIntent pin = PendingIntent.getActivity(context,0,in,0);
        builder.addAction(R.mipmap.ic_launcher,"Set time",pin);
        manager.notify(1,builder.build());
    }
}
