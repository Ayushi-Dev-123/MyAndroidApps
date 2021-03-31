
package com.example.productdetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Third extends AppCompatActivity {
    EditText etName, etPrice, etDescription;
    Button btnUpdate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.edit);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToSecond();
            }
        });
    }

    private void goBackToSecond() {
        String name = etName.getText().toString();
        String price = etPrice.getText().toString();
        String desc = etDescription.getText().toString();

        Product p = new Product(name,Integer.parseInt(price),desc);

        Intent in = new Intent();
        in.putExtra("updatedProduct",p);

        setResult(222,in);
        finish();

    }
    private void initializeFields() {
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setText("Update");
    }
}
