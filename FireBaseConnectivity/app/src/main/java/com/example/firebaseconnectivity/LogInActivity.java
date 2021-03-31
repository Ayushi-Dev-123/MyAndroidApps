package com.example.firebaseconnectivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {
    EditText etEmail, etPasspord;
    Button btnLogin, btnCreate;
    TextView tvForget;
    FirebaseDatabase mAuth;
    FirebaseUser currentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle("Login Here");

        inipComponent();
        mAuth = FirebaseDatabase.getInstance();
        //currentUser = mAuth.getCurrentUser();


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LogInActivity.this, Register_Activity.class);
                startActivity(in);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPasspord.getText().toString();
                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Please Enter Email_id");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    etPasspord.setError("Required Password");
                    return;
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser!=null){
            Intent in = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(in);
            finish();
        }
    }

    private void inipComponent() {
        tvForget = findViewById(R.id.tvForget);
        etEmail =  findViewById(R.id.etEmail);
        etPasspord = findViewById(R.id.etPassword);
        btnCreate = findViewById(R.id.btnSign);
        btnLogin = findViewById(R.id.btnLogin);
    }
}
