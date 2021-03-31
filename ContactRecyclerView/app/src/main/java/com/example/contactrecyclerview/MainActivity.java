package com.example.contactrecyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView tvName, tvNumber;
    ImageView img1,Image1;
    CardView cv1;
    RecyclerView rv;

    ArrayList<Contact> contactList;
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder> adapter;
    RecyclerView.LayoutManager manager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Contact List");
        initComponent();
        contactList = new ArrayList<>();


        contactList.add(new Contact(R.drawable.p6,"Ayushi","9009234567"));
        contactList.add(new Contact(R.drawable.p7,"Anshika","9009277567"));
        contactList.add(new Contact(R.drawable.p8,"Rishika","9009266567"));
        contactList.add(new Contact(R.drawable.p12,"Avisha","9009235567"));
        contactList.add(new Contact(R.drawable.p11,"Aisha","9009234667"));
        contactList.add(new Contact(R.drawable.p6,"Zoya","9009234666"));
        contactList.add(new Contact(R.drawable.p8,"Savi","90092345455"));
        contactList.add(new Contact(R.drawable.p7,"Nisha","9009234444"));
        contactList.add(new Contact(R.drawable.p9,"Nishta","9009234333"));
        contactList.add(new Contact(R.drawable.p10,"Shreya","9009234222"));
        contactList.add(new Contact(R.drawable.p12,"Sara","9009234111"));
        contactList.add(new Contact(R.drawable.p11,"Zara","9009234000"));
        contactList.add(new Contact(R.drawable.p7,"Isha","9009235444"));

        adapter = new ContactAdapter(MainActivity.this, contactList);
        rv.setAdapter(adapter);
        manager = new LinearLayoutManager(MainActivity.this);
        //manager = new GridLayoutManager(MainActivity.this,2);
        rv.setLayoutManager(manager);

        new ItemTouchHelper(new DeleteOnSwipe()).attachToRecyclerView(rv);
    }

    class DeleteOnSwipe extends ItemTouchHelper.SimpleCallback {
        public DeleteOnSwipe(){
            super(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
        }
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
            final Contact obj = contactList.get(position);
            contactList.remove(position);
            adapter.notifyDataSetChanged();
            Snackbar.make(rv,"Item Removed", Snackbar.LENGTH_LONG).setAction("Undo",new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    contactList.add(position,obj);
                    adapter.notifyDataSetChanged();
                }
            }).show();
        }
    }

    private void initComponent() {
        Image1 = findViewById(R.id.image1);
        tvName = findViewById(R.id.Name);
        tvNumber = findViewById(R.id.Number);
        img1 = findViewById(R.id.img1);
        cv1 = findViewById(R.id.card_view);
        rv = findViewById(R.id.rv);
    }
}