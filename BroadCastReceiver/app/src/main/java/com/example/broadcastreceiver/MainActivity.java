package com.example.broadcastreceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.broadcastreceiver.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        receiver = new MyReceiver();
        registerReceiver(receiver, new IntentFilter("com.example.broadcastreceiver.DEMO"));
        registerReceiver(new TimeChangeReceiver(),new IntentFilter(Intent.ACTION_TIME_CHANGED));

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent("com.example.broadcastreceiver.DEMO");
                sendBroadcast(in);
            }
        });

    }
}