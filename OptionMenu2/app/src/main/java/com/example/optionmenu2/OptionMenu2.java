package com.example.optionmenu2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class OptionMenu2 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Menus");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getGroupId();
        if(id == R.id.menuHome) {
            Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT);
        }
        else if(id == R.id.menuAboutus){
            Toast.makeText(this, "About us Clicked", Toast.LENGTH_SHORT);
        }
        else if(id == R.id.menuContactus){
            Toast.makeText(this, "Contact us Clicked", Toast.LENGTH_SHORT);
        }
        else if(id == R.id.menuLogin){
            Toast.makeText(this, "Login Clicked", Toast.LENGTH_SHORT);
        }
        else if(id == R.id.menuRegister){
            Toast.makeText(this, "Register Clicked", Toast.LENGTH_SHORT);
        }
        return super.onOptionsItemSelected(item);
    }
}
