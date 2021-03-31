package com.example.expense_tracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class CategoryDAO {
    public static ArrayList<Category> getCategoryList(Context context){
        ArrayList<Category> al = new ArrayList<>(5);
        DatabaseHelper helper = new DatabaseHelper(context);
        String sql = "select * from category";
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor c = database.rawQuery(sql,null);
        while(c.moveToNext()){
            int id = c.getInt(0);
            String name = c.getString(1);
            Category category = new Category(id,name);
            al.add(category);
        }
        database.close();
        return al;
    }
}
