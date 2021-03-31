package com.example.expense_tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Icon;

import java.util.ArrayList;

public class ExpenseDAO {
    public static boolean save(Expenses expenses, Context context){
        boolean status = false;
        try{
            DatabaseHelper helper = new DatabaseHelper(context);
            SQLiteDatabase database = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("category_id", expenses.getCategoryId());
            values.put("tag", expenses.getTag());
            values.put("date", expenses.getDate());
            values.put("amount", expenses.getAmount());
            values.put("payment_mode", expenses.getPaymentMode());
            database.insert("expenses", null, values);
            database.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        status = true;
        return status;
    }
    public static ArrayList<Expenses> getExpenses(Context context){
        ArrayList<Expenses> al = new ArrayList<>();
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        String sql = "select * from expenses";
        Cursor c = database.rawQuery(sql,null);
        while (c.moveToNext()){
            int id = c.getInt(0);
            String tag = c.getString(2);
            String date = c.getString(3);
            int amount = c.getInt(4);
            int categoryId = c.getInt(1);
            String paymentMode = c.getString(5);
            Expenses ex = new Expenses();
            ex.setAmount(amount);
            ex.setCategoryId(categoryId);
            ex.setDate(date);
            ex.setPaymentMode(paymentMode);
            ex.setTag(tag);
            al.add(ex);
        }
        return al;
    }

    public static boolean delete(Expenses expenses, Context context ){
        boolean status = false;
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("expenses","id = ?", new String[]{""+expenses.getId()});
        status = true;
        db.close();
        return true;
    }
}
