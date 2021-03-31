package com.example.expense_tracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expense_tracker.databinding.AddExpenseBinding;
import com.example.expense_tracker.databinding.ExpenseItemListBinding;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    Context context;
    ArrayList<Expenses> expenseList;
    AddExpenseBinding addExpenseBinding;
    OnRecyclerViewClick listener;

    public ExpenseAdapter(Context context, ArrayList<Expenses> expenseList) {
        this.expenseList = expenseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExpenseItemListBinding binding = ExpenseItemListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ExpenseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expenses ex = expenseList.get(position);
        holder.binding.tvAmount.setText("₹ " + ex.getAmount());
        holder.binding.tvDate.setText("" + ex.getDate());
        holder.binding.tvTag.setText("" + ex.getTag());
        holder.binding.tvPaymentMode.setText("" + ex.getPaymentMode());
        holder.binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog ab = new AlertDialog.Builder(context).create();
                addExpenseBinding = AddExpenseBinding.inflate(LayoutInflater.from(context));
                ab.setView(addExpenseBinding.getRoot());
                addExpenseBinding.etAmount.setText("" + ex.getAmount());
                addExpenseBinding.etTag.setText("" + ex.getTag());
                addExpenseBinding.tvDate.setText("" + ex.getDate());
                int cId = ex.getCategoryId();


                //String sql2 = "SELECT *, (SELECT category_name FROM category WHERE id = expenses.category_id)FROM exepnses WHERE id "+ cId;
                //Cursor c = database.rawQuery(sql2,null);
                //String sql1 = "SELECT * FROM expenses JOIN category USING (category_id) WHERE expenes.category_id = "+ cId;
                //String sql = "SELECT id, tag, date, amount FROM expenses join categories on expenses.category_id = category.id  WHERE category_id = "+ cId;
                addExpenseBinding.btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                ab.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        ExpenseItemListBinding binding;

        public ExpenseViewHolder(ExpenseItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Expenses e = expenseList.get(position);
                    if (position != RecyclerView.NO_POSITION && listener != null)
                        listener.onItemClick(e, position);
                }
            });
        }
    }

    public interface OnRecyclerViewClick {
        void onItemClick(Expenses expense, int position);
    }

    public void setOnItemClicklistner(OnRecyclerViewClick listener) {
        this.listener = listener;
    }
}
