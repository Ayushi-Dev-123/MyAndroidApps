package com.example.imageview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    ImageView iv;
    RadioGroup rg;
    //RadioButton  rdShinChan, rdBheem, rdDoremon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        initComponent();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rdBheem)
                    iv.setImageResource(R.drawable.bheem);
                else if (i == R.id.rdShinChan)
                    iv.setImageResource(R.drawable.shanchan);
                else if (i == R.id.rdDoremon)
                    iv.setImageResource(R.drawable.doremon);
            }
        });
    }
    private void initComponent(){
        iv = findViewById(R.id.imageView);
        rg = findViewById(R.id.rg);
    }
}