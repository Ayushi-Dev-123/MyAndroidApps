package com.example.registrationpage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etUsername, etPassWord, etCity, etEmail,etAge;
    ImageView btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerpage);
        getSupportActionBar().hide();
        initComponent();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassWord.getText().toString();
                if(username.length() == 0) {
                    etUsername.setError("Username Required");
                    return;
                }
                Toast.makeText(MainActivity.this, "Username : " + username +"\nCity : "+ etCity +"\nAge = "+etAge +"\nemail : "+etEmail,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initComponent(){
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        btnRegister = findViewById(R.id.btnRegister);
        etPassWord = findViewById(R.id.etPassword);
        etAge = findViewById(R.id.etAge);
    }
}

