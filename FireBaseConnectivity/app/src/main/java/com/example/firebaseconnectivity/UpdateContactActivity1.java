package com.example.firebaseconnectivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebaseconnectivity.model.Contact;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateContactActivity1 extends AppCompatActivity {
    EditText etName, etNumber, etEmail;
    Button btnUpdate;
    DatabaseReference contactReference;

    public UpdateContactActivity1() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_contact);

        contactReference = FirebaseDatabase.getInstance().getReference("contacts");
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etNumber = findViewById(R.id.etNumber);
        btnUpdate = findViewById(R.id.btnUpdate);

        Intent in = getIntent();
        final Contact c = (Contact)in.getSerializableExtra("contact");

        etName.setText(c.getName());
        etEmail.setText(c.getEmail());
        etNumber.setText(c.getNumber());
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String number = etNumber.getText().toString();
                String email = etEmail.getText().toString();

                Contact updatedContact = new Contact(c.getId(),name,email,number);

                contactReference.child(updatedContact.getId()).setValue(updatedContact).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(UpdateContactActivity1.this,"Contact Updated",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            String message = task.getException().toString();
                            Toast.makeText(UpdateContactActivity1.this,message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
