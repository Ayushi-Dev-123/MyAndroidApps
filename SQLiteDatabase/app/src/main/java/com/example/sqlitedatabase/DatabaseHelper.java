package com.example.sqlitedatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class

DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context,"contactdb.sqLite",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       String sql = "create table contact(id integer primary key AUTOINCREMENT, name varchar(10), email varchar(20),mobile varchar(10),age integer)";
       sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
