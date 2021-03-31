package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
     ListView lv;
    ArrayList<String> citylist;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.lv);

        citylist = new ArrayList<String>();
        citylist.add("Indore");
        citylist.add("Kolkata");
        citylist.add("Ahemdabad");
        citylist.add("Goa");
        citylist.add("Delhi");
        citylist.add("Mumbai");
        citylist.add("Dewas");
        citylist.add("Hydrabad");
        citylist.add("Noida");
        citylist.add("Channei");

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, citylist);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCity = citylist.get(i);
                Toast.makeText(MainActivity.this, selectedCity, Toast.LENGTH_SHORT);
            }
        });
    }
}