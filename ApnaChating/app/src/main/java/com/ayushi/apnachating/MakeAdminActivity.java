package com.ayushi.apnachating;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.adapter.MakeAdminAdapter;
import com.ayushi.apnachating.fragments.CreateAdminBottomSheet;
import com.ayushi.apnachating.model.Group;
import com.ayushi.apnachating.model.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MakeAdminActivity extends AppCompatActivity {

    RecyclerView rv;
    Button btnExit;
    Toolbar toolbar;
    Group group;
    String currentUserId;
    DatabaseReference groupReference, memeberReference, userReference;
    MakeAdminAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_create_group);

        initComponent();

        group = (Group)getIntent().getSerializableExtra("group");

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        groupReference = FirebaseDatabase.getInstance().getReference("Groups");
        memeberReference = FirebaseDatabase.getInstance().getReference("Groups").child(group.getGroupId()).child("member");

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setIndexedQuery(memeberReference, userReference, User.class)
                .build();
        adapter = new MakeAdminAdapter(options);
        adapter.setOnItemClicklistner(new MakeAdminAdapter.OnRecyclerViewClick() {
            @Override
            public void onItemClick(User user, int position) {
                CreateAdminBottomSheet bottomSheet = new CreateAdminBottomSheet(user, groupReference, memeberReference, group, currentUserId);
                bottomSheet.show(getSupportFragmentManager(),"");
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(MakeAdminActivity.this));
        rv.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void initComponent() {
        toolbar = findViewById(R.id.createGroupToolBar);
        toolbar.setTitle("Create new admin");
        toolbar.setSubtitle("Make admin before exit group");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = findViewById(R.id.rvCreateGroup);

        btnExit = findViewById(R.id.btnNext);
        btnExit.setVisibility(View.GONE);
    }
}
