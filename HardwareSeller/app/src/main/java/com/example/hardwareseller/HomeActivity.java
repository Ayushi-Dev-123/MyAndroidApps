package com.example.hardwareseller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.hardwareseller.adapter.CategoryAdapter;
import com.example.hardwareseller.adapter.ProductAdapter;
import com.example.hardwareseller.api.CategoryService;
import com.example.hardwareseller.api.ProductService;
import com.example.hardwareseller.api.ShopkeeperService;
import com.example.hardwareseller.bean.Category;
import com.example.hardwareseller.bean.Product;
import com.example.hardwareseller.bean.Shopkeeper;
import com.example.hardwareseller.databinding.HomeBinding;
import com.example.hardwareseller.utility.InternetConnectivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    HomeBinding binding;
    String currentUserId;
    CategoryAdapter adapter;
    String name = "";
    FirebaseAuth mAuth;
    ProductAdapter productAdapter;
    InternetConnectivity connectivity;
    SharedPreferences sp = null;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.homeToolBar);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.e("userid", "-=====>" + currentUserId);
        mAuth = FirebaseAuth.getInstance();
        sp = getSharedPreferences("user", MODE_PRIVATE);
        getNavigationDrawer();

        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvAppName.setVisibility(View.GONE);
                binding.etSearch.setVisibility(View.VISIBLE);
            }
        });


        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString().trim();
                binding.tvCategoryHeading.setVisibility(View.GONE);
                binding.rvSearch.setVisibility(View.VISIBLE);

                searchProduct();

                if(name.isEmpty()){
                    binding.tvCategoryHeading.setVisibility(View.VISIBLE);
                    binding.rvSearch.setVisibility(View.GONE);
                    binding.etSearch.setVisibility(View.GONE);
                    binding.tvAppName.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        CategoryService.CategoryApi categoryApi = CategoryService.getCategoryApiInstance();
        Call<ArrayList<Category>> call = categoryApi.getCategoryList();
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                if (response.code() == 200) {
                    ArrayList<Category> categoryList = response.body();
                    adapter = new CategoryAdapter(HomeActivity.this, categoryList);
                    binding.rv.setAdapter(adapter);
                    binding.rv.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
                    adapter.setOnItemClickListener(new CategoryAdapter.OnRecyclerClick() {
                        @Override
                        public void onItemClickListener(Category category, int position) {
                            Intent in = new Intent(HomeActivity.this, ProductActivity.class);
                            in.putExtra("category", category);
                            startActivity(in);
                        }
                    });
                } else
                    Toast.makeText(HomeActivity.this, "Innvalid response", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                Log.e("Error", "==>" + t);
            }
        });
        binding.fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeActivity.this, AddProductActivity.class);
                startActivity(in);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserProfile();
    }

    private void checkUserProfile() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String id = sp.getString("userId", "Not found");
        Log.e("status : ", "=====>" + id);
        if (!id.equals("Not found")) {
            if (!id.equals(currentUserId)) {
                ShopkeeperService.ShopkeeperApi userApi = ShopkeeperService.getStoreApiInstance();
                Call<Shopkeeper> call1 = userApi.getShopKeeperProfile(currentUserId);
                call1.enqueue(new Callback<Shopkeeper>() {
                    @Override
                    public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {
                        if (response.code() == 200) {
                            Shopkeeper user = response.body();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("shopName", user.getShopName());
                            editor.putString("userId", user.getShopkeeperId());
                            editor.putString("address", user.getAddress());
                            editor.putString("email", user.getEmail());
                            editor.putString("mobile", user.getContactNumber());
                            editor.putString("token", user.getToken());
                            editor.putString("imageUrl", user.getImageUrl());
                            editor.putString("ownerName", user.getName());
                            editor.commit();
                        } else if (response.code() == 404) {
                            sendUserToProfileActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call<Shopkeeper> call, Throwable t) {
                        Log.e("Failure", "==>" + t);
                    }
                });
            }
        } else {
            ShopkeeperService.ShopkeeperApi userApi = ShopkeeperService.getStoreApiInstance();
            Call<Shopkeeper> call2 = userApi.getShopKeeperProfile(currentUserId);
            call2.enqueue(new Callback<Shopkeeper>() {
                @Override
                public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {
                    if (response.code() == 200) {
                        Shopkeeper user = response.body();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("userId", user.getShopkeeperId());
                        editor.putString("address", user.getAddress());
                        editor.putString("email", user.getEmail());
                        editor.putString("mobile", user.getContactNumber());
                        editor.putString("shopName", user.getShopName());
                        editor.putString("token", user.getToken());
                        editor.putString("imageUrl", user.getImageUrl());
                        editor.putString("ownerName", user.getName());
                        editor.commit();
                    } else if (response.code() == 404) {
                        sendUserToProfileActivity();
                    }
                }

                @Override
                public void onFailure(Call<Shopkeeper> call, Throwable t) {
                    Log.e("Error", "==>" + t);
                }
            });
        }
    }

    private void searchProduct() {
        if (connectivity.isConnectedToInternet(HomeActivity.this)) {
            ProductService.ProductApi api = ProductService.getProductApiInstance();
            Call<ArrayList<Product>> call = api.searchProductByName(name);
            call.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    if (response.code() == 200) {
                        ArrayList<Product> productList = response.body();
                        productAdapter = new ProductAdapter(HomeActivity.this, productList);
                        binding.rvSearch.setVisibility(View.VISIBLE);
                        binding.rvSearch.setAdapter(adapter);
                        binding.rvSearch.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));

                        productAdapter.setOnItemClick(new ProductAdapter.OnRecyclerViewClick() {
                            @Override
                            public void onItemClickListener(Product product, int position) {
                                Intent in = new Intent(HomeActivity.this,ProductDetailActivity.class);
                                in.putExtra("product", product);
                                startActivity(in);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

                }
            });
        }
    }

    private void getNavigationDrawer() {
        toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.homeToolBar, R.string.open, R.string.close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menuHome) {
                    Intent in = new Intent(HomeActivity.this, HomeActivity.class);
                    startActivity(in);
                } else if (id == R.id.menuSetting) {
                    //Intent in = new Intent(HomeActivity.this, SettingActivity.class);
                    //startActivity(in);
                } else if (id == R.id.menuManageOrders) {
                    // Intent in = new Intent(HomeActivity.this, ManageOrderActivity.class);
                    //startActivity(in);
                } else if (id == R.id.menuOrderHistory) {
                    // Intent in = new Intent(HomeActivity.this, OrderHistoryActivity.class);
                    //startActivity(in);
                } else if (id == R.id.menuProfile) {
                    Intent in = new Intent(HomeActivity.this, ViewProfileActivity.class);
                    startActivity(in);
                } else if (id == R.id.menuLogout) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(HomeActivity.this);
                    ab.setMessage("Do you want to logout ?");
                    ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog pd = new ProgressDialog(HomeActivity.this);
                            pd.setTitle("Please wait...");
                            pd.show();
                            //SharedPreferences.Editor editor = sp.edit();editor.clear();editor.commit();
                            mAuth.signOut();
                            pd.dismiss();
                            sendUserToLoginActivity();
                        }
                    });
                    ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ab.show();
                } else if (id == R.id.menuContactus) {
                    Toast.makeText(HomeActivity.this, "Contactus clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.menuCustomerSupport) {
                    Toast.makeText(HomeActivity.this, "Customer support clicked", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void sendUserToProfileActivity() {
        Intent in = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(in);
    }

    private void sendUserToLoginActivity() {
        Intent in = new Intent(this, LoginActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
        finish();
    }
}
