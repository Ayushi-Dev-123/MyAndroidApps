package com.example.thanku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Thanks extends AppCompatActivity {
    TextView tvthanks, tvwish, tvfor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thanks);
        getSupportActionBar().hide();
        tvwish = findViewById(R.id.Wishes);
        tvfor = findViewById(R.id.For);
        tvthanks = findViewById(R.id.thank_you);

        tvthanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thanks();
            }
        });

        Intent in = getIntent();
    }
    private void thanks() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.ani);
        tvthanks.startAnimation(animation);
    }
}
