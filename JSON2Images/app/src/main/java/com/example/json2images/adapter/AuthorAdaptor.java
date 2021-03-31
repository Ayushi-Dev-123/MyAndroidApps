package com.example.json2images.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.json2images.Author.Author;
import com.example.json2images.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AuthorAdaptor extends RecyclerView.Adapter<AuthorAdaptor.AuthorViewHolder> {
    ArrayList<Author> authortList;

    public AuthorAdaptor(ArrayList<Author> authortList) {
        this.authortList = authortList;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new AuthorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
       Author author = authortList.get(position);
       Picasso.get().load(author.getDownload_url()).into(holder.iv);
       holder.tv.setText(author.getAuthor());
    }

    @Override
    public int getItemCount() {
        return authortList.size();
    }

    public class AuthorViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView iv;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tv = itemView.findViewById(R.id.tv);
        }
    }
}
