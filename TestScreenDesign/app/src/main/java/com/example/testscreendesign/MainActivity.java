package com.example.testscreendesign;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.animation.BounceInterpolator;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login4);
        getSupportActionBar().hide();
        Button btnLogin = findViewById(R.id.btnLogin);
        ObjectAnimator animator = ObjectAnimator.ofFloat(btnLogin,"translationY",-100f,0f);
        animator.setDuration(5000);
        animator.setInterpolator(new BounceInterpolator());
        animator.setRepeatCount(3);
        animator.start();
    }
}