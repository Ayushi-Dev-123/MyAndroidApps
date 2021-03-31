package com.example.activitylifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etUsername, etPassword, etmobileNo;
    Button btnSubmit;
    RadioButton rdMale, rdFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initComponent();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                //Integer etmobile = etmobileNo.getText().parseInt(etmobileNo);
                String password = etPassword.getText().toString();
                //String rdMale = rr.getText().toString();
                if (username.length() == 0) {
                    etUsername.setError("Username Required");
                    return;
                }
                Intent in = new Intent(MainActivity.this, SecondActivity.class);
                //in.putExtra("Data = " + username);
                in.putExtra("Username : " + username, "Password : " + password);
                startActivity(in);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart()-1", Toast.LENGTH_SHORT).show();
    }


    private void initComponent(){
        etUsername = findViewById(R.id.etUserName);
        btnSubmit = findViewById(R.id.btnSubmit);
        etPassword = findViewById(R.id.etPassword);
        rdFemale = findViewById(R.id.rdFemale);
        rdMale = findViewById(R.id.rdMale);
        etmobileNo = findViewById(R.id.etNumber);
    }
}