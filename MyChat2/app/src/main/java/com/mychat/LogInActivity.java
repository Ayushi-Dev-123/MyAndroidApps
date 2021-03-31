package com.mychat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvForgetPassowrd, tvCreate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle("Login Here");
        initComponent();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initComponent() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvCreate = findViewById(R.id.tvCreateNew);
        tvForgetPassowrd = findViewById(R.id.tvForgetPassword);
    }
}
