package com.ayushi.expensetrackor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.expensetrackor.adapter.ExpensesListAdapter;
import com.ayushi.expensetrackor.model.Expense;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditExpenseActivity extends AppCompatActivity {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firestore.collection("Expenses");
    String id = firestore.collection("Expenses").document().getId();
    DocumentReference documentReference = firestore.collection("Expenses").document(id);

    TextView tvAmount, tvCategory, tvTag,tvDate;
    EditText etAmount, etTag, etDate, etCategory;
    Button btnAdd, btnCancle;

    private static final String KEY_CATEGORY = "category";
    private static final String KEY_TAG = "tag";
    private static final String KEY_DATE = "date";
    private static final String KEY_AMOUNT = "amount";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense_dialog);
        tvAmount = findViewById(R.id.tvAmount);
        tvTag = findViewById(R.id.tvTag);
        tvDate = findViewById(R.id.tvDate);
        tvCategory = findViewById(R.id.tvCategory);
        etAmount = findViewById(R.id.etAmount);
        etCategory = findViewById(R.id.etCategory);
        etDate = findViewById(R.id.etDate);
        etTag = findViewById(R.id.etTag);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancle = findViewById(R.id.btncancle);

        Intent in = getIntent();
        Expense expense = (Expense) in.getSerializableExtra("expense");

        etAmount.setText(expense.getAmount());
        etCategory.setText(expense.getCategory());
        etTag.setText(expense.getTag());
        etDate.setText(expense.getDate());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = etTag.getText().toString();
                String category = etCategory.getText().toString();
                String date = etDate.getText().toString();
                String amount = etAmount.getText().toString();

                Expense expense = new Expense(tag,category,amount,date);

                Map<String, Object> expenses = new HashMap<>();
                expenses.put(KEY_DATE, date);
                expenses.put(KEY_TAG, tag);
                expenses.put(KEY_CATEGORY, category);
                expenses.put(KEY_AMOUNT, amount);

                documentReference.set(expense)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                                Toast.makeText(EditExpenseActivity.this, "Expense Saved", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditExpenseActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                //Log.e(TAG, e.toString());
                            }
                });
            }
        });
       // readData();
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /*private void readData() {
        FirestoreRecyclerOptions<Expense> options = new FirestoreRecyclerOptions.Builder<Expense>()
                .setQuery(collectionReference, Expense.class)
                .build();
        adapter = new ExpensesListAdapter(options, this);
        adapter.startListening();
        rv.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
                Toast.makeText(MainActivity.this, "Expense removed", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rv);
    }*/
}
