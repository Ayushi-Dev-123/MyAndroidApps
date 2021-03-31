package com.example.expense_tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

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
}
