package com.example.listviewshopsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.listviewshopsearch.R.layout.shopdetails;

public class ShopAdapter extends ArrayAdapter {
    Context context;
    ArrayList<ShopDetails> al;

    public ShopAdapter(Context context,ArrayList<ShopDetails> al) {
        super(context, R.layout.shopdetails, al);
        this.context = context;
        this.al = al;
    }
    /*
    public ShopAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ShopDetails> al) {
        super(context, shopdetails, al);
        this.context = context;
        this.al = al;
    }
    */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ShopDetails shop = al.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.shopdetails,null);

        TextView shopName = v.findViewById(R.id.ShopName);
        TextView shopAddress = v.findViewById(R.id.ShopAddress);
        TextView shopLocation = v.findViewById(R.id.location);
        TextView shopnow = v.findViewById(R.id.tvshopnow);

       // ListView lvList = v.findViewById(R.id.lvList);

        shopAddress.setText(shop.getAddress());
        shopName.setText(shop.getName());
        shopLocation.setText(shop.getLocation());

        return v;
    }
}
