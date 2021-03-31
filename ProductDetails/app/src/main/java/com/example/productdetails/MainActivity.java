package com.example.productdetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText etName, etPrice, etDescription;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initComponent();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToShowActivity();
            }
        });
    }
    private void sendDataToShowActivity() {
        String name = etName.getText().toString();
        String price = etPrice.getText().toString();
        String desc = etDescription.getText().toString();

        Product p = new Product(name,Integer.parseInt(price),desc);

        Intent showActivityIntent = new Intent(MainActivity.this ,  Second.class);
        showActivityIntent.putExtra("product",p);
        startActivity(showActivityIntent);
    }

    private void initComponent(){
        etDescription = findViewById(R.id.etDescription);
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        btnSave = findViewById(R.id.btnSave);
    }
}