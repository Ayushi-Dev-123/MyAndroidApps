package com.example.productdetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Second extends AppCompatActivity {
    TextView tvName, tvPrice,tvDescription;
    Button btnEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);
        getSupportActionBar().hide();

        initComponent();
        Intent in = getIntent();
        Product p = (Product) in.getSerializableExtra("product");
        tvName.setText(p.getName());
        tvPrice.setText(""+p.getPrice());
        tvDescription.setText(p.getDescription());

        String name = in.getStringExtra("product_name");
        String price = in.getStringExtra("product_price");
        String description = in.getStringExtra("product_description");

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendDataToUpdateActivity();
            }
        });
    }
    private void sendDataToUpdateActivity() {
        String name = tvName.getText().toString();
        String price = tvPrice.getText().toString();
        String desc = tvDescription.getText().toString();

        Product p = new Product(name,Integer.parseInt(price),desc);
        Intent updateActivityIntent = new Intent(Second.this, Third.class);
        updateActivityIntent.putExtra("product",p);
        startActivityForResult(updateActivityIntent,111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "onActivity-Result", Toast.LENGTH_SHORT).show();
        if(resultCode == 222){
            Product p = (Product) data.getSerializableExtra("updatedProduct");
            tvName.setText(p.getName());
            tvPrice.setText(""+p.getPrice());
            tvDescription.setText(p.getDescription());
        }
    }

    private void initComponent() {
        tvDescription = findViewById(R.id.etDescription);
        tvName = findViewById(R.id.etName);
        tvPrice = findViewById(R.id.etPrice);
        btnEdit = findViewById(R.id.btnEdit);
    }
}
