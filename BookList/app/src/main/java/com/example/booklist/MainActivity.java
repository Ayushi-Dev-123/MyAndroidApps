package com.example.booklist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    ImageView img;
    //EditText etSearch;

    //RecyclerView.Adapter<AdapterClass.BookViewHolder> adapter;
    //RecyclerView.LayoutManager manager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Books Search");

        img = findViewById(R.id.img);
        rv = findViewById(R.id.rv);
        //etSearch = findViewById(R.id.etSearch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("C");
        menu.add("CPP");
        menu.add("JAVA");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        int id = item.getItemId();
        if(title.equals("C"))
        {
            ArrayList<BookList> cBooks = new ArrayList<>();
            cBooks.add(new BookList(R.drawable.c1,400,"C"));
            cBooks.add(new BookList(R.drawable.c10,449,"C in Depth"));
            cBooks.add(new BookList(R.drawable.c9,399,"Expert C Programming"));
            cBooks.add(new BookList(R.drawable.c8,499,"Let US C"));
            cBooks.add(new BookList(R.drawable.c7,399,"The Complete Reference"));
            cBooks.add(new BookList(R.drawable.c6,620,"C Programming"));
            cBooks.add(new BookList(R.drawable.c4,289,"C"));
            cBooks.add(new BookList(R.drawable.c3,450,"C Programming Language"));
            cBooks.add(new BookList(R.drawable.c1,299,"ANCII C"));
            cBooks.add(new BookList(R.drawable.c2,399,"The C Programming Language"));

          AdapterClass adapter =  new AdapterClass(MainActivity.this, cBooks);
            rv.setAdapter(adapter);
           LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
           // LinearLayoutManager manager = new GridLayoutManager(MainActivity.this,2);
            rv.setLayoutManager(manager);
        }
        else if(title.equals("CPP"))
        {
            ArrayList<BookList> cppBooks = new ArrayList<>();
            cppBooks.add(new BookList(R.drawable.cp1,567,"The Complete Reference"));
            cppBooks.add(new BookList(R.drawable.cp2,254,"C++ Programming"));
            cppBooks.add(new BookList(R.drawable.cp3,634,"C++ Programming Language"));
            cppBooks.add(new BookList(R.drawable.cp4,523,"C++"));
            cppBooks.add(new BookList(R.drawable.cp5,399,"C++"));
            cppBooks.add(new BookList(R.drawable.cp6,499,"CPP Programming"));
            cppBooks.add(new BookList(R.drawable.cp8,268,"CPP"));
            cppBooks.add(new BookList(R.drawable.cp9,430,"C++"));
            cppBooks.add(new BookList(R.drawable.cp10,475,"Objet Oriented Programming C++"));

            AdapterClass adapter =  new AdapterClass(MainActivity.this, cppBooks);
            rv.setAdapter(adapter);
            LinearLayoutManager manager;
            manager = new LinearLayoutManager(MainActivity.this);
            //manager = new GridLayoutManager(MainActivity.this,2);
            rv.setLayoutManager(manager);
        }
        else if(title.equals("JAVA"))
        {
            ArrayList<BookList> javaBooks = new ArrayList<>();
            javaBooks.add(new BookList(R.drawable.j10,445,"Java"));
            javaBooks.add(new BookList(R.drawable.j9,350,"Core Java"));
            javaBooks.add(new BookList(R.drawable.j8,710,"Java Black Book"));
            javaBooks.add(new BookList(R.drawable.j7,269,"Java"));
            javaBooks.add(new BookList(R.drawable.j6,399,"Java Programming"));
            javaBooks.add(new BookList(R.drawable.j4,267,"Java Programming"));
            javaBooks.add(new BookList(R.drawable.j5,500,"Java Programming"));
            javaBooks.add(new BookList(R.drawable.j3,400,"JAVA2"));
            javaBooks.add(new BookList(R.drawable.j2,380,"Java Complete Reference"));
            javaBooks.add(new BookList(R.drawable.j1,499,"Java Programming"));

            AdapterClass adapter =  new AdapterClass(MainActivity.this, javaBooks);
            rv.setAdapter(adapter);
            LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
            //LinearLayoutManager manager = new GridLayoutManager(MainActivity.this,2);
            rv.setLayoutManager(manager);
        }
         return super.onOptionsItemSelected(item);
    }
}