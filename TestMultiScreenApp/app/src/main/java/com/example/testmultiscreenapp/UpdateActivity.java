package com.example.testmultiscreenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {
    EditText etName,etPrice,etDesc;
    Button btnUpdate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeFields();

        Intent in = getIntent();
        Product p = (Product)in.getSerializableExtra("product");
        etName.setText(p.getName());
        etPrice.setText(""+p.getPrice());
        etDesc.setText(p.getDescription());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              goBackToShowActivity();
            }
        });
    }

    private void goBackToShowActivity() {
       String name = etName.getText().toString();
       String price = etPrice.getText().toString();
       String desc = etDesc.getText().toString();

       Product p = new Product(name,Integer.parseInt(price),desc);

       Intent in = new Intent();
       in.putExtra("updatedProduct",p);

       setResult(222,in);
       finish();
    }

    private void initializeFields() {
      etName = findViewById(R.id.etName);
      etPrice = findViewById(R.id.etPrice);
      etDesc = findViewById(R.id.etDesc);
      btnUpdate = findViewById(R.id.btnSave);
      btnUpdate.setText("Update");
    }
}
