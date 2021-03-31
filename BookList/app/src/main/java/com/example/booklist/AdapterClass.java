package com.example.booklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
//import com.mikhaellopez.circularimageview.CircularImageView;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.BookViewHolder>
{
    ArrayAdapter<BookList> adapter;
    Context context;
    ArrayList<BookList> al;
    public AdapterClass(Context context, ArrayList<BookList>al) {
        this.context = context;
        this.al = al;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.view, parent, false);
        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookList bl = al.get(position);
        holder.img.setImageResource(bl.getImg());
        holder.tvPrice.setText(""+bl.getPrice());
        holder.tvName.setText(bl.getName());
    }

    public int getItemCount() {
        return al.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        RecyclerView rv;
        ImageView img;
        //CircularImageView civ;
        //EditText etSearch;
        public BookViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
          //  civ = v.findViewById(R.id.circularImageView);
            tvPrice = v.findViewById(R.id.tvPrice);
           // etSearch = v.findViewById(R.id.etSearch);
            rv = v.findViewById(R.id.rv);
            img = v.findViewById(R.id.img);
        }
    }
}
