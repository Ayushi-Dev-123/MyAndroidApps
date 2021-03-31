package com.example.testmultiscreenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ShowActivity extends AppCompatActivity {
    TextView tvName,tvPrice,tvDescription;
    Button btnEdit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_activity);
        initComponent();

        Intent in = getIntent();
        Product p = (Product) in.getSerializableExtra("product");
        tvName.setText(p.getName());
        tvPrice.setText(""+p.getPrice());
        tvDescription.setText(p.getDescription());
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

       Intent updateActivityIntent = new Intent(ShowActivity.this, UpdateActivity.class);
       updateActivityIntent.putExtra("product",p);
       startActivityForResult(updateActivityIntent,111);
    }

    private void initComponent() {
      tvName = findViewById(R.id.tvName);
      tvPrice = findViewById(R.id.tvPrice);
      tvDescription = findViewById(R.id.tvDesc);
      btnEdit = findViewById(R.id.btnEdit);
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
}
