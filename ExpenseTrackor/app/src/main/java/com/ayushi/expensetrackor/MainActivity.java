package com.ayushi.expensetrackor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayushi.expensetrackor.adapter.ExpensesListAdapter;
import com.ayushi.expensetrackor.databinding.AddExpenseDialogBinding;
import com.ayushi.expensetrackor.model.Category;
import com.ayushi.expensetrackor.model.Expense;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.FirestoreClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    ArrayList<Expense> expenseList;
    FirebaseFirestore firestore;
    ExpensesListAdapter adapter;
    private static final String TAG = "MainActivity";
    CollectionReference collectionReference;
    DocumentReference documentReference;
    AddExpenseDialogBinding binding;
    ArrayList<String> paymentMode;
    ArrayList<Category> categoryList;

    private static final String KEY_CATEGORY = "category";
    private static final String KEY_TAG = "tag";
    private static final String KEY_DATE = "date";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_PAYMENTMODE = "paymentmode";
    private Object QuerySnapshot;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = AddExpenseDialogBinding.inflate(LayoutInflater.from(this));

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setHasFixedSize(true);

        this.firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("Expenses");
        String id = firestore.collection("Expenses").document().getId();
        documentReference = firestore.collection("Expenses").document(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Add expenses");
        menu.add("Total Expenses");
        menu.add("Log out");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        if (title.equals("Add expenses")) {
            final AlertDialog ab = new AlertDialog.Builder(MainActivity.this).create();
            ab.setTitle("Add expenses");
            View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_expense_dialog, null);
            final EditText etCategory = v.findViewById(R.id.etCategory);
            final EditText etAmount = v.findViewById(R.id.etAmount);
            final EditText etTag = v.findViewById(R.id.etTag);
            final EditText etDate = v.findViewById(R.id.etDate);
            Button btnCancle = v.findViewById(R.id.btncancle);
            Button btnAdd = v.findViewById(R.id.btnAdd);
            ab.setView(v);

            paymentMode = new ArrayList<>();
            paymentMode.add("Online");
            paymentMode.add("Cash");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,paymentMode);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.SPaymentOption.setAdapter(arrayAdapter);

            categoryList = new ArrayList<>();
            //Firestore fireStore = FirestoreClient.getFirestore();
            //ApiFuture<QuerySnapshot> apiFuture =
            QuerySnapshot = firestore.collection("Category").get();

            firestore.collection("Category").get();

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = etTag.getText().toString();
                    String category = etCategory.getText().toString();
                    String date = etDate.getText().toString();
                    String amount = etAmount.getText().toString();

                    Expense expense = new Expense();
                    Map<String, Object> expenses = new HashMap<>();
                    expenses.put(KEY_DATE, date);
                    expenses.put(KEY_TAG, tag);
                    expenses.put(KEY_CATEGORY, category);
                    expenses.put(KEY_AMOUNT, amount);
                    expenses.put(KEY_PAYMENTMODE, paymentMode);
                    documentReference.set(expenses)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    ab.dismiss();
                                    Toast.makeText(MainActivity.this, "Expense Saved", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, e.toString());
                                }
                            }); //setRecyclerView();
                }
            });
            readData();
            btnCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ab.dismiss();
                }
            });

            ab.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void readData() {
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
    }
}

