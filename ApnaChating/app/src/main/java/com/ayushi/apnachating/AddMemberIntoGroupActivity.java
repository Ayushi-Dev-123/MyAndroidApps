package com.ayushi.apnachating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.adapter.AddMemberIntoGroupAdapter;
import com.ayushi.apnachating.model.Group;
import com.ayushi.apnachating.model.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddMemberIntoGroupActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv;
    Button btnAdd;
    Group group;
    String currentUserId;
    DatabaseReference userReference, contactReference, groupReference, memberReference;
    AddMemberIntoGroupAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_create_group);

        Intent in = getIntent();
        group =(Group) in.getSerializableExtra("group");

        initComponent();

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        contactReference = FirebaseDatabase.getInstance().getReference("Contacts");
        groupReference = FirebaseDatabase.getInstance().getReference("Groups");
        memberReference = groupReference.child(group.getGroupId()).child("member");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setIndexedQuery(contactReference,userReference,User.class)
                .build();
        adapter = new AddMemberIntoGroupAdapter(options, group);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(AddMemberIntoGroupActivity.this));
        //int count = rv.getChildCount();
       // Toast.makeText(this, ""+count, Toast.LENGTH_SHORT).show();
        adapter.startListening();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(AddMemberIntoGroupActivity.this);
                pd.setTitle("Please Wait..");
                pd.setMessage("While adding member into the group");
                pd.show();

                ArrayList<User> al = adapter.getSelectUserList();
                for(final User user : al ){
                    groupReference.child(group.getGroupId()).child(user.getUid()).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               memberReference.child(user.getUid()).setValue(user.getName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                      pd.dismiss();
                                      if(task.isSuccessful()){
                                          Toast.makeText(AddMemberIntoGroupActivity.this, "Member Added", Toast.LENGTH_SHORT).show();
                                           finish();
                                      }
                                      else
                                          Toast.makeText(AddMemberIntoGroupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                   }
                               });
                           }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void initComponent() {
        toolbar = findViewById(R.id.createGroupToolBar);
        toolbar.setTitle("Add Participants");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv = findViewById(R.id.rvCreateGroup);
        btnAdd = findViewById(R.id.btnNext);
        btnAdd.setText("Add");
    }
}
