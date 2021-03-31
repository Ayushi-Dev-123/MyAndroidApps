package com.ayushi.apnachating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class LauncherActivity extends AppCompatActivity {

    ProgressDialog pd;
    Button btnLogin, btnSignin;
    TextView tvApnaChat;
    CircleImageView cvlogo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);

        //setSupportActionBar(toolBar).hide;

        btnLogin = findViewById(R.id.btnLogin);
        btnSignin = findViewById(R.id.btnSignIn);
        cvlogo = findViewById(R.id.civLogo);
        tvApnaChat = findViewById(R.id.tvApnaChat);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(LauncherActivity.this);
                pd.setTitle("Login...");
                pd.setMessage("Please Wait...");
                pd.show();
                sendUserToLoginActivity();
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(LauncherActivity.this);
                pd.setTitle("Signing...");
                pd.setMessage("Please Wait...");
                pd.show();
                sendUserToRegisterActivity();

            }
        });
    }

    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(LauncherActivity.this,LogInActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pd.dismiss();
        startActivity(loginIntent);
        finish();
    }

    private void sendUserToRegisterActivity() {
        Intent signinIntent = new Intent(LauncherActivity.this,RegisterActivity.class);
        signinIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pd.dismiss();
        startActivity(signinIntent);
        finish();
    }

}
