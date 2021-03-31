package com.example.expense_tracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    public DatabaseHelper(Context context){
       super(context,"expense_tracker.sqlite",null,1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table expenses(id integer primary key AUTOINCREMENT,category_id integer, tag, varchara(100)," +
                " date varchar(100), amount integer, payment_mode varchar(7))";
        db.execSQL(sql);

        sql = "create table category(id integer primary key AUTOINCREMENT, category_name varchar(100))";
        db.execSQL(sql);

        sql = "insert into category(category_name) values('Select category'),('Travelling'),('Fuel'),('Mobile Recharge'),('Food'),('Other')";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
