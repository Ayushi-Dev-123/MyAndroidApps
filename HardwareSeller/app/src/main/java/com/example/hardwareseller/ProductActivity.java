package com.example.hardwareseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.hardwareseller.adapter.ProductAdapter;
import com.example.hardwareseller.api.ProductService;
import com.example.hardwareseller.bean.Category;
import com.example.hardwareseller.bean.Product;
import com.example.hardwareseller.databinding.ProductActivityBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    ProductActivityBinding binding;
    Product product;
    Category category;
    String  currentUserId;
    ProductAdapter adapter;
    ArrayList<Product> productList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProductActivityBinding.inflate(LayoutInflater.from(ProductActivity.this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        category = (Category) in.getSerializableExtra("category");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ProductService.ProductApi productApi = ProductService.getProductApiInstance();
        Call<List<Product>> call = productApi.getProductByCategoryAndShopKeeper(category.getCategoryId(), currentUserId);
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("please wait ...");
        pd.show();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                pd.dismiss();
                if(response.code() == 200){
                    productList = (ArrayList<Product>) response.body();
                    adapter = new ProductAdapter(ProductActivity.this, productList);
                    binding.rvProduct.setAdapter(adapter);
                    binding.rvProduct.setLayoutManager(new GridLayoutManager(ProductActivity.this,2));
                    adapter.setOnItemClick(new ProductAdapter.OnRecyclerViewClick() {
                        @Override
                        public void onItemClickListener(Product product, int position) {
                            Intent in = new Intent(ProductActivity.this,ProductDetailActivity.class);
                            in.putExtra("productDescription",product );
                            startActivity(in);
                        }
                    });
                }
                else
                    Toast.makeText(ProductActivity.this, response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(ProductActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
