package com.ayushi.expensetrackor;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    TextView tvName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        tvName = findViewById(R.id.tvExpense);
        AnimationUtils.loadAnimation(SplashActivity.this, R.anim.translate);
        new SplashThread().start();
    }
    class SplashThread extends Thread{
        @Override
        public void run() {
            super.run();
            try{
               Thread.sleep(5000);
                Intent in = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(in);
                finish();
            }
            catch(Exception e){

            }
        }
    }
}
