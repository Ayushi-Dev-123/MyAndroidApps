package com.example.optionmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.optionmenu2);
        //ActionBar ab = getSupportActionBar();
        setTitle("Social Media");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        menu.add("Facebook");
        menu.add("Whatsapp");
        menu.add("Instagram");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        int id = item.getItemId();
        if(title.equals("Facebook")) {
            if(id == R.id.menuLogin) {

            }
             else if(id == R.id.menuSignin) {

            }
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
            startActivity(in);
        }
        if(title.equals("Whatsapp")) {
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.whatsapp.com/"));
            startActivity(in);
        }
        if(title.equals("Instagram")) {
            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/"));
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }
}