package com.ayushi.apnachating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText etEmail, etPassword, confirmPassword;
    Button btnCreate;
    FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();

        initComponent();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd = new ProgressDialog(RegisterActivity.this);
                pd.setTitle("Creating...");
                pd.setMessage("Please Wait...");
                pd.show();

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Email id required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    etPassword.setError("Password is required");
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           sendUserToMainActivity();
                       }
                       else{
                           String message = task.getException().toString();
                           Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();
                       }
                    }
                });
            }
        });
    }

    private void sendUserToMainActivity(){
        Intent mainActivityIntent = new Intent(RegisterActivity.this,MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivityIntent);
        finish();
    }

    private void initComponent(){
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnCreate = findViewById(R.id.btnCreate);
       // confirmPassword = findViewById(R.id.confirmPassword);
    }
}
