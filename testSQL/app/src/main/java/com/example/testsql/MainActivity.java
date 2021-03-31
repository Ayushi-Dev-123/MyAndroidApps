package com.example.testsql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testsql.databinding.ActivityMainBinding;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<User> userList;
    UserAdapter adapter;
    DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        helper = new DatabaseHelper(MainActivity.this);
        userList = getUsers(helper);
        adapter = new UserAdapter(this,userList);
        binding.rvUser.setAdapter(adapter);
        binding.rvUser.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
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
            AlertDialog ab = new AlertDialog.Builder(MainActivity.this).create();
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.add_user,null);
            ab.setView(view);
            EditText etName = view.findViewById(R.id.etname);
            EditText etAge = view.findViewById(R.id.etage);
            EditText etMobile = view.findViewById(R.id.etmobile);
            EditText etEmail = view.findViewById(R.id.etemail);
            CardView btnCancel = view.findViewById(R.id.btnCancel);
            CardView btnOkay = view.findViewById(R.id.btnOkay);

            btnOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String age = etAge.getText().toString();
                    String mobile = etMobile.getText().toString();

                    SQLiteDatabase db = helper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put("name",name);
                    values.put("age",age);
                    values.put("mobile",mobile);
                    values.put("email",email);

                    User user = new User();
                    user.setAge(age);
                    user.setEmail(email);
                    user.setMobile(mobile);
                    user.setName(name);

                    long result = db.insert("user",null,values);
                    if(result == -1){
                        Log.e("Not insert","===>"+result);
                    }else{
                        Log.e("result","==>"+result);
                        Toast.makeText(MainActivity.this, "contact saved", Toast.LENGTH_SHORT).show();
                        //db.close();
                        ab.dismiss();
                    }
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ab.dismiss();
                }
            });

            ab.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<User> getUsers(DatabaseHelper helper) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<User> userList = new ArrayList<>();
        String sql = "select * from user";
        Cursor c = db.rawQuery(sql,null,null);
        while (c.moveToNext()){
            int id = c.getInt(0);
            String name = c.getString(1);
            String email = c.getString(2);
            String mobile = c.getString(3);
            String age = c.getString(4);
            User user = new User(name,email,mobile,age,id);
            userList.add(user);
            //adapter.notifyDataSetChanged();
        }
        return userList;
    }

}