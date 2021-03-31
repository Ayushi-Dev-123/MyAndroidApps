package com.example.sqlitedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sqlitedatabase.databinding.ActivityMainBinding;
import com.example.sqlitedatabase.databinding.AddcontactsBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DatabaseHelper helper;
    ArrayList<Contact> contactList;
    ContactListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        helper = new DatabaseHelper(MainActivity.this);
        contactList = ContactDAO.getContactList(helper);
        adapter = new ContactListAdapter(this,contactList);
        binding.rv.setAdapter(adapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Add contacts");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        if(title.equals("Add contacts")){
            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
            final AddcontactsBinding contactsBinding = AddcontactsBinding.inflate(LayoutInflater.from(MainActivity.this));
            View v = contactsBinding.getRoot();
            ab.setView(v);
            ab.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        String name = contactsBinding.etName.getText().toString();
                        String email = contactsBinding.etEmail.getText().toString();
                        String mobile = contactsBinding.etMobile.getText().toString();
                        String age = contactsBinding.etAge.getText().toString();
                        SQLiteDatabase db = helper.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put("name",name);
                        values.put("email",email);
                        values.put("age",age);
                        values.put("mobile",mobile);

                        //db.insert("contact",null,values);
                        //Toast.makeText(MainActivity.this, "Contact Saved", Toast.LENGTH_SHORT).show();
                        //Contact contact = new Contact(name,email,mobile,Integer.parseInt(age));

                        Contact contact =  new Contact();
                        contact.setAge(Integer.parseInt(age));
                        contact.setEmail(email);
                        contact.setMobile(mobile);
                        contact.setName(name);
                        boolean status = ContactDAO.saved(contact,helper);
                        if(status) {
                             Toast.makeText(MainActivity.this, "Contact Saved", Toast.LENGTH_SHORT).show();
                             contactList.add(contact);
                             adapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        /*
                        String sql = "insert into contact(name, email,mobile,age) values('" + name + "','" + email + "','" + mobile + "','" + age + "')";
                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.execSQL(sql);
                        Toast.makeText(MainActivity.this, "Contact Saved", Toast.LENGTH_SHORT).show();
                        */
                    }
                    catch (Exception e){
                        Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                        Log.e("Exception : ","==>" + e);
                    }

                }
            });
            ab.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                }
            });
            ab.show();
        }
        return super.onOptionsItemSelected(item);
    }
}