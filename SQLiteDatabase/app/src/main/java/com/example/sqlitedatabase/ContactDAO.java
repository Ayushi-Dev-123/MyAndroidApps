package com.example.sqlitedatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ContactDAO {
    public static boolean saved(Contact contact, DatabaseHelper helper){
       boolean status = false;
       try{
           ContentValues values = new ContentValues();
           values.put("name",contact.getName());
           values.put("email",contact.getEmail());
           values.put("mobile",contact.getMobile());
           values.put("age",contact.getAge());
           SQLiteDatabase db = helper.getWritableDatabase();
           db.insert("contact",null,values);
           status = true;
       }
       catch (Exception e){
          e.printStackTrace();
       }
       return status;
    }
    public static ArrayList<Contact> getContactList(DatabaseHelper databaseHelper){
        ArrayList<Contact> contactList = new ArrayList<>();
        String sql = "select * from contact";
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql,null,null);
        while (c.moveToNext()){
            int id = c.getInt(0);
            String name = c.getString(1);
            String mobile = c.getString(2);
            String email = c.getString(3);
            int age = c.getInt(4);
            Contact contact = new Contact(id,name,mobile,email,age);
            contactList.add(contact);
        }
        return contactList;
    }
    public static boolean delete(Contact c,DatabaseHelper helper){
        boolean status = false;
        try{
            SQLiteDatabase db = helper.getWritableDatabase();
            // delete from contact where id = 4
            db.delete("contact","id = ?", new String[]{""+c.getId()});
            status = true;
            db.close();
        }
        catch (Exception e){
           e.printStackTrace();
        }
        return status;
    }

    public static boolean update(Contact contact, DatabaseHelper databaseHelper){
        boolean status = false;
        try{
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", contact.getName());
            values.put("email",contact.getEmail());
            values.put("mobile",contact.getMobile());
            values.put("age", contact.getAge());
            // update contact set name='xyz',mobile='2939495950',email='xyz@gmail.com',age=10 where id = 4
            db.update("contact",values,"Id=?", new String[]{""+ contact.getId()});
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }
}
