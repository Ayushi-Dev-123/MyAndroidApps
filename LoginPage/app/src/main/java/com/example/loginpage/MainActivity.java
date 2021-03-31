package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText etUsername, etPassWord;
    ImageView btnLogIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);
        getSupportActionBar().hide();
        initComponent();
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                //String password = etPassWord.getText().toString();
                if(username.length() == 0) {
                    etUsername.setError("Username Required");
                    return;
                }
                Toast.makeText(MainActivity.this, "Username : " + username, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initComponent(){
        etUsername = findViewById(R.id.etUserName);
        btnLogIn = findViewById(R.id.btnLogIn);
        etPassWord = findViewById(R.id.etPassword);
    }
}