package com.example.testsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context,"userdb",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table user(id integer primary key AUTOINCREMENT,name varchar(50), email varchar(20), mobile varchar(10), age integer )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+"user");
        onCreate(db);
    }

   /* public static boolean addContact(User user, DatabaseHelper helper){
        ContentValues values  ;
        return true;
    }*/

}
