package com.example.retrofittest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.retrofittest.databinding.ActivityMainBinding;

import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    List<Product> list;
    ProductAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        ProductApi.ProductService productService = ProductApi.getProductService();
        final Call<List<Product>> productList = productService.getProductList();

        productList.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                list = response.body();
                if(list.size() > 0){
                    Toast.makeText(MainActivity.this, "" + list, Toast.LENGTH_SHORT).show();
                     adapter = new ProductAdapter(MainActivity.this,list);

                     adapter.setOnItemClick(new ProductAdapter.onRecyclerViewClick() {
                         @Override
                         public void onItemClick(Product p, int position) {
                             Call<Product> productCall = ProductApi.getProductService().getProduct(3);
                             productCall.enqueue(new Callback<Product>() {
                                 @Override
                                 public void onResponse(Call<Product> call, Response<Product> response) {
                                     Product p = response.body();
                                     Toast.makeText(MainActivity.this, p.getId()+" "+p.getName(), Toast.LENGTH_SHORT).show();
                                 }

                                 @Override
                                 public void onFailure(Call<Product> call, Throwable t) {

                                 }
                             });
                         }
                     });
                     binding.rv.setAdapter(adapter);
                     binding.rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Add product");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        if(title.equals("Add product")){
           ProductApi.ProductService productService = ProductApi.getProductService();
           Product p = new Product();
           p.setId(200);
           p.setName("XYZ Product");
           p.setQty(10);
           p.setDescription("xyz description");

           Call<Product> call = productService.saveProduct(p);
           call.enqueue(new Callback<Product>() {
               @Override
               public void onResponse(Call<Product> call, Response<Product> response) {
                   Product p = response.body();
                   Toast.makeText(MainActivity.this, "Product saved", Toast.LENGTH_SHORT).show();
                   list.add(p);
                   adapter.notifyDataSetChanged();
               }

               @Override
               public void onFailure(Call<Product> call, Throwable t) {

               }
           });
        }
        return super.onOptionsItemSelected(item);
    }
}