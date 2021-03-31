package com.example.listviewshopsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

     private ListView lv;
    TextView tvShopName, tvShopAddress, tvShopLocation, tvstar, tvarrow, tvshop;
    ArrayList<ShopDetails> shopList;
    ArrayAdapter<ShopDetails> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initComponent();
        shopList = new ArrayList<>();

        shopList.add(new ShopDetails("Pick N Move","South Tokoganj","2.5km"));
        shopList.add(new ShopDetails("EasyDay","Palasia","3km"));
        shopList.add(new ShopDetails("DMart","Rajwada","5km"));
        shopList.add(new ShopDetails("More Maga Store","Bangali","1km"));
        shopList.add(new ShopDetails("Uttam","Vijay Nagar","7km"));
        shopList.add(new ShopDetails("Relience Fresh","Geeta Bhawan","3.7km"));
        shopList.add(new ShopDetails("BigBazar","MG Road","8km"));
        shopList.add(new ShopDetails("OnDoor","Super Coridor","10km"));
        shopList.add(new ShopDetails("Metro","AB Road","12km"));
        shopList.add(new ShopDetails("Best Price","By Pass","9k"));

        adapter = new ShopAdapter(MainActivity.this,shopList);
        lv.setAdapter(adapter);
    }
    private void initComponent() {
        tvShopAddress = findViewById(R.id.ShopAddress);
        tvShopName = findViewById(R.id.ShopName);
        tvShopLocation = findViewById(R.id.location);
        lv = findViewById(R.id.lvList);
        tvstar = findViewById(R.id.tvstar);
        tvarrow = findViewById(R.id.tvarrow);
    }
}















