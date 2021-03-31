package com.ayushi.apnachating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LogInActivity extends AppCompatActivity {

    ProgressDialog pd;
    TextInputLayout inputEmail, inputPassword;
    EditText etEmail, etPassword;
    Button btnLogin;
    DatabaseReference userReference;
    String currentUserId;
    TextView tvForgetPassowrd, tvCreate, tvLogin;
    CircleImageView civpersonLogo ;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.login);
        initComponent();
        Intent in = getIntent();

        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(LogInActivity.this);
                pd.setTitle("Authenticating...");
                pd.setMessage("Please Wait...");
                pd.show();

                String email =  inputEmail.getEditText().getText().toString();
                String password = inputPassword.getEditText().getText().toString();

                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Email id required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    etPassword.setError("Password is required");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if(task.isSuccessful()){
                            sendUserToMainActivity();
                        }
                        else{
                            String message = task.getException().toString();
                            Toast.makeText(LogInActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(LogInActivity.this);
                pd.setTitle("Going");
                pd.setMessage("Please Wait...");
                pd.show();

                sendUserToRegisterActivity();
            }
        });
        tvForgetPassowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(LogInActivity.this);
                pd.setTitle("Logging...");
                pd.setMessage("Please Wait...");
                pd.show();
                //
                FirebaseAuth.getInstance().setLanguageCode("en");
                FirebaseAuth.getInstance().sendPasswordResetEmail("indorikepohe@gmail.com").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.e("Email Send","==>");
                        }
                        else{
                            String mes = task.getException().toString();
                            Toast.makeText(LogInActivity.this, mes, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //
                sendUserToForgetPasswordActivity();
            }
        });
    }

    private void sendUserToForgetPasswordActivity() {
        Intent forget = new Intent(LogInActivity.this, ForgetPasswordActivity.class);
        forget.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(forget);
        finish();
    }

    private void sendUserToRegisterActivity() {
        Intent register = new Intent(LogInActivity.this,RegisterActivity.class);
        register.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(register);
        finish();
    }

    private void sendUserToMainActivity() {
       Intent main = new Intent(LogInActivity.this,MainActivity.class);
       main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(main);
       finish();
    }


    private void initComponent() {
        //etEmail = findViewById(R.id.etEmail);
        //etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvCreate = findViewById(R.id.tvCreateNew);
        tvForgetPassowrd = findViewById(R.id.tvForgetPassword);
        tvLogin = findViewById(R.id.tvLogin);
        civpersonLogo = findViewById(R.id.personLogo);

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        //progressBar = findViewById(R.id.llProgressDialog);
    }


   /*
    FirebaseUser currentUser;
    private static final int RC_SIGN_IN = 123;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "SignIn Success", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(LogInActivity.this, MainActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                finish();
            }
            else{
                Toast.makeText(this, "SignIn Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}
