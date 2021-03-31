package com.example.expense_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.expense_tracker.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        ArrayList<Category> al = CategoryDAO.getCategoryList(this);
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,al);
        binding.spinner.setAdapter(adapter);
        binding.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DatePickerDialog dp = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       date = dayOfMonth + "-"+(month+1)+"-"+year;
                       binding.tvDate.setText(date);
                   }
               },2021,2,3);
               dp.show();
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = binding.etTag.getText().toString();
                String amount = binding.etAmount.getText().toString();
                String paymentMode = "";
                if(TextUtils.isEmpty(tag)){
                    binding.etTag.setError("Enter tag");
                }
                else if(TextUtils.isEmpty(amount)){
                    binding.etAmount.setText("Enter amount");
                }
                if(binding.rbCash.isChecked())
                    paymentMode = "By cash";
                else if(binding.rbOnline.isChecked())
                    paymentMode = "By online";
                Category c = (Category) binding.spinner.getSelectedItem();
                int id = 0;
                if(!c.getCategoryName().equalsIgnoreCase("Select category")){
                    id = c.getId();
                }
                if(id ==0)
                    return;
                Expenses expenses = new Expenses();
                expenses.setTag(tag);
                expenses.setAmount(Integer.parseInt(amount));
                expenses.setPaymentMode(paymentMode);
                expenses.setCategoryId(id);
                expenses.setDate(date);

                boolean status = ExpenseDAO.save(expenses,MainActivity.this);
                if(status == true)
                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}