package com.ayushi.apnachating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ayushi.apnachating.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SendRequestActivity extends AppCompatActivity {

    CircleImageView civProfile;
    Button btnRequest;
    TextView tvUsername,tvStatus;
    Toolbar toolbar;
    String currentUserId, receiverUserId;
    DatabaseReference chatRequestReference, contactReference;
    ProgressDialog pd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_request);

        initComponent();

        Intent in = getIntent();
        User user = (User) in.getSerializableExtra("user");

        receiverUserId = user.getUid();  //Receiver User

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();   //Sender User

        chatRequestReference = FirebaseDatabase.getInstance().getReference("ChatRequest");
        contactReference = FirebaseDatabase.getInstance().getReference("Contacts");

        if(currentUserId.equals(receiverUserId)){
            btnRequest.setVisibility(View.GONE);
            toolbar.setTitle("You");
        }

        contactReference.child(currentUserId).child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    btnRequest.setVisibility(View.GONE);
                    toolbar.setTitle("Already Friend");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tvStatus.setText(user.getStatus());
        tvUsername.setText(user.getName());
        Picasso.get().load(user.getImage()).placeholder(R.drawable.chat_logo2).into(civProfile);

        checkRequestStatus();

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String requestStatus = btnRequest.getText().toString();
                if(requestStatus.equalsIgnoreCase("Send Request"))
                    sendRequest();
                else if(requestStatus.equalsIgnoreCase("Cancle Request"))
                    cancleRequest();
            }
        });
    }

    private void sendRequest() {
        chatRequestReference.child(currentUserId).child(receiverUserId).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if(task.isSuccessful()){
                   chatRequestReference.child(receiverUserId).child(currentUserId).child("request_type").setValue("receive").addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           Toast.makeText(SendRequestActivity.this, "Request Send", Toast.LENGTH_SHORT).show();
                           btnRequest.setText("Cancle Request");
                           toolbar.setTitle("Cancle Request");
                       }
                   });
               }
            }
        });
    }

    private void cancleRequest() {
        chatRequestReference.child(currentUserId).child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                chatRequestReference.child(receiverUserId).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SendRequestActivity.this, "Request Cancelled", Toast.LENGTH_SHORT).show();
                        btnRequest.setText("Send Request");
                        toolbar.setTitle("Send Request");
                    }
                });
            }
        });
    }

    private void checkRequestStatus(){
        chatRequestReference.child(currentUserId).child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String requestType = dataSnapshot.child("request_type").getValue().toString();
                    if(requestType.equalsIgnoreCase("send")){
                        btnRequest.setText("Cancle Request");
                        toolbar.setTitle("Cancle Request");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initComponent() {
        civProfile = findViewById(R.id.civProfile);
        tvStatus = findViewById(R.id.status);
        tvUsername = findViewById(R.id.userName);
        btnRequest = findViewById(R.id.btnSendRequest);

        toolbar = findViewById(R.id.sendRequestToolbar);
        toolbar.setTitle("Send Request");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
