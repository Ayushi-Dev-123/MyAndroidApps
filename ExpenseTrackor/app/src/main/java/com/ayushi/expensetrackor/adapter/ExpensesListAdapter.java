package com.ayushi.expensetrackor.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.expensetrackor.EditExpenseActivity;
import com.ayushi.expensetrackor.MainActivity;
import com.ayushi.expensetrackor.R;
import com.ayushi.expensetrackor.model.Expense;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpensesListAdapter extends FirestoreRecyclerAdapter<Expense, ExpensesListAdapter.ExpenseListViewHolder> {
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firestore.collection("Expenses");
    String id = firestore.collection("Expenses").document().getId();
    DocumentReference documentReference = firestore.collection("Expenses").document(id);

    private static final String KEY_CATEGORY = "category";
    private static final String KEY_TAG = "tag";
    private static final String KEY_DATE = "date";
    private static final String KEY_AMOUNT = "amount";

    private onRecyclerViewClick listener;
    Context context;
    public ExpensesListAdapter(@NonNull FirestoreRecyclerOptions<Expense> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ExpenseListViewHolder holder, final int position, @NonNull final Expense expense) {
        holder.etAmount.setText(expense.getAmount());
        holder.etCategory.setText(expense.getCategory());
        holder.etDate.setText(expense.getDate());
        holder.etTag.setText(expense.getTag());
    }

    @NonNull
    @Override
    public ExpenseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_list,parent,false);
        return new ExpenseListViewHolder(v);
    }

    public class ExpenseListViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategory, tvAmount, tvTag, tvDate, etCategory, etAmount, etTag, etDate;
        ImageView ivEdit;
        public ExpenseListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTag = itemView.findViewById(R.id.tvTag);
            etAmount = itemView.findViewById(R.id.etAmount);
            etCategory = itemView.findViewById(R.id.etCategory);
            etTag = itemView.findViewById(R.id.etTag);
            etDate = itemView.findViewById(R.id.etDate);

        }
    }
    public interface onRecyclerViewClick{
        void OnItemClick();
    }
    public void setOnItemClick(onRecyclerViewClick listener){
        this.listener = listener;
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }
}
