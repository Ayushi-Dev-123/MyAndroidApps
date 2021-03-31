package com.example.checkbox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    CheckBox chkpizza, chkBurger, chkFF;
    Button btnOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        initComponent();
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int total = 0;
                String itemList ="";
                if(chkBurger.isChecked()){
                    total = total + 150;
                    itemList = itemList +  "Burger : 150\n";
                }
                if(chkpizza.isChecked()){
                    total = total + 400;
                    itemList = itemList + "Pizza : 400\n";
                }
                if(chkFF.isChecked()){
                    total = total + 200;
                    itemList = itemList+"Freanch-Fry : 200\n";
                }
                itemList = itemList + "Total : "  + total;
                Toast.makeText(MainActivity.this, itemList, Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void initComponent(){
        chkBurger = findViewById(R.id.chkBurger);
        chkFF = findViewById(R.id.chkFranchFry);
        chkpizza = findViewById(R.id.chkPizza);
        btnOrder = findViewById(R.id.btnOrder);
    }
}