package com.example.navigationdrawer1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawer;
    NavigationView navigation;
    ActionBarDrawerToggle toggle;
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        navigation.setItemIconTintList(null);
        setSupportActionBar(toolBar);

        toggle = new ActionBarDrawerToggle(this, drawer, toolBar,R.string.open,R.string.close);
        toggle.syncState();

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawer(GravityCompat.START);
                int id = item.getItemId();
                if(id == R.id.Security)
                    Toast.makeText(MainActivity.this,"Security Clicked" +
                            "",Toast.LENGTH_SHORT).show();
                else if(id == R.id.Social){
                    Toast.makeText(MainActivity.this,"Social Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.Send){
                    Toast.makeText(MainActivity.this,"Send Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.Snoooz){
                    Toast.makeText(MainActivity.this,"Snooz Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.Setting){
                    Toast.makeText(MainActivity.this,"Setting Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.contacts){
                    Toast.makeText(MainActivity.this,"Contacts Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.Important){
                    Toast.makeText(MainActivity.this,"Important Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.Schedule){
                    Toast.makeText(MainActivity.this,"Schedule Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.calender){
                    Toast.makeText(MainActivity.this,"Calender Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.Delete){
                    Toast.makeText(MainActivity.this,"Delete Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.Mail){
                    Toast.makeText(MainActivity.this,"Mail Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.Starred){
                    Toast.makeText(MainActivity.this,"Starred Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.Drafts){
                    Toast.makeText(MainActivity.this,"Draft Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.ChangePassword){
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentlayout, new ChangePasswordFragment());
                    transaction.commit();
                }
                return false;
            }
        });
    }

    public void initComponent(){
        drawer = findViewById(R.id.drawer);
        navigation = findViewById(R.id.navigation);
        toolBar = findViewById(R.id.toolbar);
    }
}