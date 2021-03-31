package com.example.addition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText et1, et2;
    Button btnAdd;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addition);

        et1  = findViewById(R.id.etFirstNumber);
        et2 = findViewById(R.id.etSecondNumber);
        btnAdd = findViewById(R.id.Result1);
        tvResult = findViewById(R.id.tvResult);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = et1.getText().toString();
                String s2 = et2.getText().toString();
                double n1 = Double.parseDouble(s1);
                double n2 = Double.parseDouble(s2);
                double n3 = n1 + n2;
                tvResult.setText("Addtion = " + n3);
            }
        });
    }
}