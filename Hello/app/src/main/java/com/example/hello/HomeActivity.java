package com.example.hello;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    EditText et;
    Button btn;
    TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        et = findViewById(R.id.etName);
        btn = findViewById(R.id.button);
        tv = findViewById(R.id.tvResult);
        btn.setOnClickListener(new TestListener());
    }
    class TestListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String name = et.getText().toString();
            tv.setText("Welcome : " + name);
        }
    }
}
