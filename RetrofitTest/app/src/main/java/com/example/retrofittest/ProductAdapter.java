package com.example.retrofittest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofittest.databinding.ProductItemlistBinding;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Context context;
    List<Product> list;
    onRecyclerViewClick listener;
    public ProductAdapter(Context context, List list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemlistBinding binding = ProductItemlistBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
       Product p = list.get(position);
       holder.binding.tvDeatils.setText("Id : "+p.getId()+"\nName : "+p.getName()+"\nPrice : "+p.getPrice()+"\nQuantity : "+p.getQty()+"\nDescription : "+p.getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        ProductItemlistBinding binding;
        public ProductViewHolder(ProductItemlistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Product p = list.get(position);
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(p,position);
                    }
                }
            });
        }
    }
    public interface onRecyclerViewClick{
        void onItemClick(Product p, int position);
    }
    public void setOnItemClick(onRecyclerViewClick listener){
        this.listener = listener;
    }
}
