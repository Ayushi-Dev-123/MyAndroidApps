package com.example.firebaseconnectivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebaseconnectivity.adapter.ContactAdapter;
import com.example.firebaseconnectivity.model.Contact;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    CardView cv;
    RecyclerView rv;
    ArrayList<Contact> al;
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder> adapter;
    RecyclerView.LayoutManager manager;
    DatabaseReference contactReference;
    FirebaseDatabase mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Contacts");
        rv = findViewById(R.id.rv);
        cv = findViewById(R.id.cv);
        mAuth = FirebaseDatabase.getInstance();

        new ItemTouchHelper(new DeleteOnSwipe(0,ItemTouchHelper.RIGHT)).attachToRecyclerView(rv);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        contactReference = database.getReference("contacts");

        getDataFromContactNode();

    }
    private void getDataFromContactNode(){
        contactReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("Data ==> ", ""+dataSnapshot);
                if (dataSnapshot.exists()) {
                    al = new ArrayList<>();
                    Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();
                    while (itr.hasNext()) {
                        DataSnapshot contact = itr.next();
                        Contact c = contact.getValue(Contact.class);
                        al.add(c);
                    }
                    adapter = new ContactAdapter(al,MainActivity.this);
                    manager = new LinearLayoutManager(MainActivity.this);
                    rv.setLayoutManager(manager);
                    rv.setAdapter(adapter);
                }
                else{
                  //  String message = task.getException().toString();
                    //Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();

                }
            }
           @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("New Contact");
        menu.add("Log Out");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        if(title.equals("New Contact")){
            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
            View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_contact,null);
            final EditText etname = v.findViewById(R.id.etName);
            final EditText etemail = v.findViewById(R.id.etEmail);
            final EditText etnumber = v.findViewById(R.id.etNumber);
            ab.setView(v);
            ab.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    String name = etname.getText().toString();
                    String email = etemail.getText().toString();
                    String number = etnumber.getText().toString();
                    /*
                    HashMap<String, String> hs = new HashMap<>();
                    hs.put("name",name);
                    hs.put("email",email);
                    hs.put("number",number);
                    */
                    String contactKey = contactReference.push().getKey();
                    Contact c = new Contact(contactKey,name,number,email);

                    contactReference.child(contactKey).setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(MainActivity.this,"Contact Saved",Toast.LENGTH_SHORT).show();
                            else{
                                String message = task.getException().toString();
                                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    /*
                    contactReference.setValue(hs).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(MainActivity.this,"Contact Saved",Toast.LENGTH_SHORT).show();
                            else{
                                String message = task.getException().toString();
                                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                   */
                }
            });
            ab.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ab.show();
        }
        else if(title.equals("Log Out")){
            //mAuth.signOut();
            Intent in = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(in);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class DeleteOnSwipe extends ItemTouchHelper.SimpleCallback
    {
        public DeleteOnSwipe(int dragDirs, int swipDirs) {
            super(dragDirs, swipDirs);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            final Contact c = al.get(position);
            contactReference.child(c.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        al.remove(position);
                        adapter.notifyDataSetChanged();
                        Snackbar.make(rv,"Contact Removed", Snackbar.LENGTH_LONG).setAction("Undo",new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                al.add(position,c);
                                adapter.notifyDataSetChanged();
                            }
                        }).show();
                    }
                    else{
                        String message = task.getException().toString();
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}