package com.example.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import static com.example.contact.R.layout.contact;


public class ContactAdapter extends ArrayAdapter {
    Context context;
    ArrayList<ContactDetails>al;

    public ContactAdapter(Context context,ArrayList<ContactDetails> al) {
        super(context, R.layout.contact, al);
        this.context = context;
        this.al = al;
    }
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ContactDetails shop = al.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(contact,null);

        TextView tvName = v.findViewById(R.id.Name);
        TextView tvNumber = v.findViewById(R.id.Number);

        // ListView lvList = v.findViewById(R.id.lvList);

        tvName.setText(shop.getName());
        tvNumber.setText(shop.getNumber());

        return v;
    }
}
