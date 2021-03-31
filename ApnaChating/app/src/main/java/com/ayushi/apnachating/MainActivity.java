package com.ayushi.apnachating;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.ayushi.apnachating.adapter.TabAccessorAdapter;
import com.ayushi.apnachating.model.Stories;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.ayushi.apnachating.adapter.TabAccessorAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolBar;
    TextView tvTitile;
    CircleImageView civCurrentUser;
    TabLayout tabLayout;
    ViewPager viewPager;
    TabAccessorAdapter adapter;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference userReference, contactReference, storiesReference;
    String currentUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();

        Intent in = getIntent();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        contactReference = FirebaseDatabase.getInstance().getReference("Contacts");
        storiesReference = FirebaseDatabase.getInstance().getReference("Stories");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser == null){
            sendUserToLogInActivity();
        }
        else{
            updateUserOnlineState("Online");
            checkUserStatus();
            getFriendsId();
            //get24hoursOldStory();
            delete24hoursOldStories(currentUser.getUid());
        }
    }

    private void getFriendsId() {
       contactReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                   Iterator<DataSnapshot> itr = snapshot.getChildren().iterator();
                   while (itr.hasNext()){
                       DataSnapshot ds = itr.next();
                       String friendId = ds.getKey();
                       delete24hoursOldStories(friendId);
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    private void delete24hoursOldStories(final String userId) {
       storiesReference.child(userId).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                   Iterator<DataSnapshot> itr = snapshot.getChildren().iterator();
                   while (itr.hasNext()){
                       DataSnapshot ds = itr.next();
                       Stories story = ds.getValue(Stories.class);
                       long stime = story.getTimeStamp();
                       Calendar calendar = Calendar.getInstance();
                       long ctime = calendar.getTimeInMillis();
                       if((ctime - stime) >= 86400000) {
                           storiesReference.child(userId).child(story.getStoryId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(!task.isSuccessful()) {
                                       Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
                       }
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(currentUser != null){
            updateUserOnlineState("Offline");
        }
    }

    private void checkUserStatus() {
        currentUserId = currentUser.getUid();
        userReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("name").exists()){
                    //Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                    tvTitile.setText(dataSnapshot.child("name").getValue().toString());
                    if(dataSnapshot.child("image").exists()){
                        String imageUrl = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(imageUrl).into(civCurrentUser);
                    }
                }
                else{
                    sendUserToSettingActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Find Friends");
        menu.add("Create Group");
        menu.add("Setting");
        menu.add("LogOut");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        if(title.equals("Find Friends")){
            Intent in = new Intent(MainActivity.this,FindFriendActivity.class);
            startActivity(in);
        }
        else if(title.equals("Create Group")){
            Intent in = new Intent(MainActivity.this, CreateGroupActivity.class);
            startActivity(in);
           // Toast.makeText(this, "Create Group Clicked", Toast.LENGTH_SHORT).show();
        }
        else if(title.equals("Setting")){
            sendUserToSettingActivity();
        }
        else if(title.equals("LogOut")){
            updateUserOnlineState("Offline");
            mAuth.signOut();
            sendUserToLogInActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendUserToSettingActivity() {
        Intent setting = new Intent(MainActivity.this, SettingActivity.class);
        setting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setting);
        finish();
    }

    private void initComponent() {
        toolBar = findViewById(R.id.mainToolBar);
        toolBar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolBar);
        // getSupportActionBar().setTitle("Apna Chatting");
        civCurrentUser = findViewById(R.id.civCurrentUser);
        tvTitile = findViewById(R.id.tvTitile);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager = findViewById(R.id.viewPager);

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

    public void updateUserOnlineState(String state){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            String currentUserId = currentUser.getUid();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
            String date = sd.format(calendar.getTime());

            sd = new SimpleDateFormat("hh:mm a");
            String time = sd.format(calendar.getTime());

            HashMap<String, Object> hm = new HashMap<>();
            hm.put("date",date);
            hm.put("time",time);
            hm.put("state",state);

            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users");
            usersReference.child(currentUserId).updateChildren(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        String errormessage = task.getException().toString();
                        Log.e("Error","==>"+errormessage);
                    }
                }
            });
        }
    }
}
