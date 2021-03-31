package com.example.expense_tracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expense_tracker.databinding.ActivityMainBinding;
import com.example.expense_tracker.databinding.AddExpenseBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    AddExpenseBinding binding;
    String date = "";
    ExpenseAdapter adapter;
    ActivityMainBinding mainBinding;
    ArrayList<Expenses> expensesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(mainBinding.getRoot());

        expensesArrayList = ExpenseDAO.getExpenses(this);
        adapter = new ExpenseAdapter(MainActivity.this,expensesArrayList);
        mainBinding.rvHistory.setAdapter(adapter);
        mainBinding.rvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();

        adapter.setOnItemClicklistner(new ExpenseAdapter.OnRecyclerViewClick() {
            @Override
            public void onItemClick(Expenses expense, int position) {
                ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        Toast.makeText(MainActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        Toast.makeText(MainActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                        //Remove swiped item from list and notify the RecyclerView
                        boolean statue = ExpenseDAO.delete(expense,MainActivity.this);
                        if(statue) {
                            int position = viewHolder.getAdapterPosition();
                            expensesArrayList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                };
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add("History");
        menu.add("Add Expense");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        /*if(title.equals("History")){
            ArrayList<Expenses> expensesArrayList = ExpenseDAO.getExpenses(this);
            adapter = new ExpenseAdapter(MainActivity.this,expensesArrayList);
            mainBinding.rvHistory.setVisibility(View.VISIBLE);
            mainBinding.rvHistory.setAdapter(adapter);
            mainBinding.rvHistory.setLayoutManager(new LinearLayoutManager(this));
        }*/
        if(title.equals("Add Expense")){
            AlertDialog ab = new AlertDialog.Builder(MainActivity.this).create();
            binding = AddExpenseBinding.inflate(LayoutInflater.from(this));
            ab.setView(binding.getRoot());

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

                    boolean status = ExpenseDAO.save(expenses, MainActivity.this);
                    if(status == true) {
                        ab.dismiss();
                        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
            binding.btnCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ab.dismiss();
                }
            });
            ab.show();

            /*binding.btnSave.setOnClickListener(new View.OnClickListener() {
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

                    boolean status = ExpenseDAO.save(expenses, MainActivity.this);
                    if(status == true)
                        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });*/
        }
        return super.onOptionsItemSelected(item);
    }
}