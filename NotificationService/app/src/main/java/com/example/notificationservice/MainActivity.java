package com.example.notificationservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.notificationservice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    NotificationManager manager;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String channelName = "TEstChannel";
                String channelId = "Test Id";
                manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationChannel channel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT);
                    manager.createNotificationChannel(channel);
                }
                NotificationCompat.Builder nb = new NotificationCompat.Builder(MainActivity.this,channelId);
                nb.setContentTitle("Test");
                nb.setContentText("First Notification");
                nb.setSmallIcon(R.mipmap.ic_launcher);

                Intent in = new Intent(Intent.ACTION_DIAL);
                PendingIntent pin = PendingIntent.getActivity(MainActivity.this,111,in, PendingIntent.FLAG_UPDATE_CURRENT);
                nb.addAction(R.mipmap.ic_launcher,"Call",pin);
                manager.notify(1,nb.build());
            }
        });
    }
}