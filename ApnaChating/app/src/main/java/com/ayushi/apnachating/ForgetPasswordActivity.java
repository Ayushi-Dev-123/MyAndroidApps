package com.ayushi.apnachating;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    ProgressDialog pd;
    TextView tvClose, tvResetPassword;
    EditText etEmail;
    Button btnSet;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mAuth = FirebaseAuth.getInstance();

        final AlertDialog ab = new AlertDialog.Builder(this).create();
        View v = LayoutInflater.from(this).inflate(R.layout.forget_password,null);

        tvClose = v.findViewById(R.id.tvCancle);
        etEmail = v.findViewById(R.id.etEmail);
        btnSet = v.findViewById(R.id.btnSetPassword);

        ab.setView(v);
        ab.show();

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ab.dismiss();
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
  /*              pd = new ProgressDialog(ForgetPasswordActivity.this);
                pd.setTitle("Setting Password...");
                pd.setMessage("Please Wait...");
                pd.show();
*/
                String email = etEmail.getText().toString();
                Toast.makeText(ForgetPasswordActivity.this, "Email Send", Toast.LENGTH_SHORT).show();
                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Required!");
                    return;
                }
                FirebaseAuth.getInstance().setLanguageCode("en");
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful()){
                             Log.e("Email Send","==>");
                         }
                         else{
                             String mes = task.getException().toString();
                             Toast.makeText(ForgetPasswordActivity.this, mes, Toast.LENGTH_SHORT).show();
                         }
                    }
                });
                /*.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });*/
            }
        });
    }
}
