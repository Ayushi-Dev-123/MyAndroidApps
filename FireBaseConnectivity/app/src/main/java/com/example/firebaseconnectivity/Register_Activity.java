package com.example.firebaseconnectivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Activity extends AppCompatActivity {
    EditText etEmail,etPasspord,etCreate, etConfirm;
    Button btnCreate;
    FirebaseDatabase mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        setTitle("Register Here");

        inipComponent();

        mAuth = FirebaseDatabase.getInstance();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPasspord.getText().toString();
                String confirm = etConfirm.getText().toString();
                /*
                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Please Enter Email_id");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    etPasspord.setError("Required Password");
                    return;
                }
                if(TextUtils.isEmpty(confirm)){
                    etPasspord.setError("Required");
                    return;
                }*/
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompletelistner(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent in = new Intent(Register_Activity.this,MainActivity.class);
                            startActivity(in);
                            finish();
                        }
                    }
                });
            }
        });
    }

    private void inipComponent() {
        etConfirm = findViewById(R.id.etConfirmPassword);
        etEmail =  findViewById(R.id.etEmail);
        etPasspord = findViewById(R.id.etPassword);
        btnCreate = findViewById(R.id.btnSign);

    }
}
