package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView tvName, tvNumber;
    ListView lv;

    ArrayList<ContactDetails> contactList;
    ArrayAdapter<ContactDetails> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        contactList = new ArrayList<>();

        contactList.add(new ContactDetails("Ayushi","9098908111"));
        contactList.add(new ContactDetails("Prachi","9098908778"));
        contactList.add(new ContactDetails("Akshat","9098908898"));
        contactList.add(new ContactDetails("Avisha","9098908999"));
        contactList.add(new ContactDetails("Ankit","9098908888"));
        contactList.add(new ContactDetails("Harshita","9098908777"));
        contactList.add(new ContactDetails("Mickey","9098908102"));
        contactList.add(new ContactDetails("Kartik","90989080101"));
        contactList.add(new ContactDetails("Anupam","9098908666"));
        contactList.add(new ContactDetails("Rishika","9098908555"));
        contactList.add(new ContactDetails("Malka","9098908444"));
        contactList.add(new ContactDetails("Devika","9098908333"));
        boolean shreya = contactList.add(new ContactDetails("Shreya", "9098908222"));

        adapter = new ContactAdapter(MainActivity.this,contactList);
        lv.setAdapter(adapter);
    }
    private void initComponent(){
        tvName = findViewById(R.id.Name);
        tvNumber = findViewById(R.id.Number);
        lv = findViewById(R.id.lv);
    }
}