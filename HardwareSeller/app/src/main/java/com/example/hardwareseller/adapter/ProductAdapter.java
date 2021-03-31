package com.example.hardwareseller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwareseller.bean.Product;
import com.example.hardwareseller.databinding.ProductScreenItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Context context;
    ArrayList<Product>al;
    OnRecyclerViewClick listener;
    public ProductAdapter(Context context, ArrayList<Product>al){
        this.context = context;
        this.al = al;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductScreenItemBinding binding = ProductScreenItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = al.get(position);
        Picasso.get().load(product.getImageUrl()).into(holder.binding.productImage);
        holder.binding.tvProductName.setText(""+product.getName());
        holder.binding.tvProductPrice.setText("₹ "+product.getPrice());
        //holder.binding.tvProductDiscount.setText(""+product.getDiscount());

        Log.e("Image","===>"+product.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ProductScreenItemBinding binding;
        public ProductViewHolder(ProductScreenItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Product p = al.get(position);
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClickListener(p,position);
                    }
                }
            });
        }
    }
    public interface OnRecyclerViewClick{
        public void onItemClickListener(Product product, int position);
    }
   public void setOnItemClick(OnRecyclerViewClick listener){
        this.listener = listener;
   }

}
