package com.example.fragment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    TextureView tvChat, tvStatus, tvCalls;
    FragmentManager manager;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
        tvCalls = findViewById(R.id.Calls);
        tvChat = findViewById(R.id.Chat);
        tvStatus = findViewById(R.id.tvStatus);

        tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.layout.chat);
            }
        });
    }
}