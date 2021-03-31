package com.mychat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.mychat.adapter.TabAccessorAdapter;

public class MainActivity extends AppCompatActivity {

    Toolbar toolBar;
    TabLayout tabLayout;
    ViewPager viewPager;
    TabAccessorAdapter adapter;
    FirebaseAuth mAuth;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponenet();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser == null){
            sendUserToLogInActivity();
        }
        else{

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Find Friends");
        menu.add("Create Group");
        menu.add("Setting");
        menu.add("LogOut");
        return super.onCreateOptionsMenu(menu);
    }

    private void initComponenet() {
        toolBar = findViewById(R.id.mainToolBar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        // setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("My Chat");

        adapter = new TabAccessorAdapter(getSupportFragmentManager(),1);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void sendUserToLogInActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LogInActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}

