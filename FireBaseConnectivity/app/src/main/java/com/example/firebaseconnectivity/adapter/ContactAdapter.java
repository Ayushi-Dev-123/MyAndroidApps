package com.example.firebaseconnectivity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseconnectivity.R;
import com.example.firebaseconnectivity.UpdateContactActivity1;
import com.example.firebaseconnectivity.model.Contact;
import com.example.firebaseconnectivity.update.UpdateContactActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    ArrayList<Contact> al;
    Context context;
    public ContactAdapter(ArrayList<Contact> al, Context context) {
        this.al = al;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list,parent,false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
       final Contact c = al.get(position);
       holder.tvemail.setText(c.getEmail());
       holder.tvname.setText(c.getName());
       holder.tvnumber.setText(c.getNumber());
       holder.ivedit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent in = new Intent(context, UpdateContactActivity1.class);
               in.putExtra("contact",c);
               context.startActivity(in);
           }
       });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvname, tvemail, tvnumber;
        ImageView ivedit;
        Button btnupdate;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            ivedit = itemView.findViewById(R.id.ivEdit);
            tvname = itemView.findViewById(R.id.tvname);
            tvnumber = itemView.findViewById(R.id.tvnumber);
            tvemail = itemView.findViewById(R.id.tvemail);
            btnupdate = itemView.findViewById(R.id.btnUpdate);
        }
    }
}
