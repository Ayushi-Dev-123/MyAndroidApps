package com.example.hardwareseller;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.hardwareseller.api.CategoryService;
import com.example.hardwareseller.api.ProductService;
import com.example.hardwareseller.bean.Category;
import com.example.hardwareseller.bean.Product;
import com.example.hardwareseller.databinding.AddProductBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class AddProductActivity extends AppCompatActivity {
    AddProductBinding binding;
    ArrayList<Category> categoryList;
    ArrayAdapter<Category> adapter;
    String shopkeeperId, categoryId;
    Category category;
    Uri imageUri;
    Uri secondImageUri;
    Uri thirdImageuri;
    ProgressDialog progressDialog;
    AlertDialog.Builder ab;
    boolean isSpinnerTouched = false;
    AwesomeValidation awesomeValidation;
    MultipartBody.Part body, body2, body3;
    ArrayList<String> brandList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddProductBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        CategoryService.CategoryApi categoryApi = CategoryService.getCategoryApiInstance();
        Call<ArrayList<Category>> call = categoryApi.getCategoryList();
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                if (response.code() == 200) {
                    categoryList = response.body();
                    categoryList.add(0, new Category("0", "Select category", ""));
                    adapter = new ArrayAdapter<Category>(AddProductActivity.this, R.layout.spinner_item_list, R.id.tvCategory, categoryList);
                    binding.spinnerCategory.setAdapter(adapter);
                    binding.spinnerCategory.setSelection(0);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

            }
        });
        shopkeeperId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        awesomeValidation = new AwesomeValidation(BASIC);
        awesomeValidation.addValidation(binding.etProductName, "[^\\s*$][a-zA-Z\\s]+", "Enter correct name");
        // awesomeValidation.addValidation(binding.etProductName, "[^\\s*$][a-zA-Z\\s]+", "Enter correct product name");
        awesomeValidation.addValidation(binding.etProductBrand, "[^\\s*$][a-zA-Z\\s]+", "Enter correct Brand name");
        awesomeValidation.addValidation(binding.etProductPrice, "[^\\\\s*$][0-9\\\\s]+", "Enter price");
        awesomeValidation.addValidation(binding.etProductQtyInStock, "[^\\s*$][0-9]*$", "Enter quantitiy");

        categorySpinner();
        addProduct();
        addProductImage();
        /*
        binding.ivProductImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddProductActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
                } else {
                    Intent in = new Intent();
                    in.setAction(Intent.ACTION_GET_CONTENT);
                    in.setType("image/*");
                    startActivityForResult(in, 111);
                }
            }
        });
        /*
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    String name = binding.etProductName.getText().toString();
                    String price = binding.etProductPrice.getText().toString();
                    String brand = binding.etProductBrand.getText().toString();
                    String discount = binding.etProductDiscount.getText().toString();
                    String desc = binding.etProductDescription.getText().toString();
                    String qty = binding.etProductQtyInStock.getText().toString();

                    if (imageUri != null) {
                        File file = FileUtils.getFile(AddProductActivity.this, imageUri);
                        RequestBody requestFile = RequestBody.create(
                                MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))),
                                file);

                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                        RequestBody productName = RequestBody.create(MultipartBody.FORM, name);
                        RequestBody productPrice = RequestBody.create(MultipartBody.FORM, price);
                        RequestBody productBrand = RequestBody.create(MultipartBody.FORM, brand);
                        RequestBody productDiscount = RequestBody.create(MultipartBody.FORM, discount);
                        RequestBody productDescription = RequestBody.create(MultipartBody.FORM, desc);
                        RequestBody productQty = RequestBody.create(MultipartBody.FORM, qty);
                        RequestBody skId = RequestBody.create(MultipartBody.FORM, shopkeeperId);
                        RequestBody catId = RequestBody.create(MultipartBody.FORM, category.getCategoryId());

                        ProgressDialog pd = new ProgressDialog(AddProductActivity.this);
                        pd.setMessage("Please wait while adding product");
                        pd.show();
                        ProductService.ProductApi productApi = ProductService.getProductApiInstance();
                        Call<Product> call = productApi.saveProduct(body, catId, skId, productName, productPrice, productBrand, productQty, productDescription, productDiscount);
                        call.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                pd.dismiss();
                                if (response.code() == 200) {
                                    Toast.makeText(AddProductActivity.this, "Product saved", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(AddProductActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Product> call, Throwable t) {
                                Toast.makeText(AddProductActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        });
                    } else
                        Toast.makeText(AddProductActivity.this, "Image is mendatory", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(AddProductActivity.this, "Plz select product image", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            binding.ivProductImage1.setImageURI(imageUri);
        }
    }
*/
    }

    private void addProduct() {
        //setSupportActionBar(activityAddProductBinding.toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Add Product");
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    if (awesomeValidation.validate()) {
                        String productname = binding.etProductName.getText().toString().trim();
                        String productprice = binding.etProductPrice.getText().toString().trim();
                        String discount = binding.etProductDiscount.getText().toString().trim();
                        String brand = binding.etProductBrand.getText().toString().trim();
                        String quantity = binding.etProductQtyInStock.getText().toString().trim();
                        String description = binding.etProductDescription.getText().toString().trim();
                        if (imageUri != null && secondImageUri != null && thirdImageuri != null) {
                            if (imageUri != null && secondImageUri != null && thirdImageuri != null) {

                                File file = FileUtils.getFile(AddProductActivity.this, imageUri);
                                RequestBody requestFile = RequestBody.create(
                                        MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))),
                                        file);
                                body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                                File file2 = FileUtils.getFile(AddProductActivity.this, secondImageUri);
                                RequestBody requestFile2 = RequestBody.create(
                                        MediaType.parse(Objects.requireNonNull(getContentResolver().getType(secondImageUri))),
                                        file2);
                                body2 = MultipartBody.Part.createFormData("file2", file2.getName(), requestFile2);

                                File file3 = FileUtils.getFile(AddProductActivity.this, thirdImageuri);
                                RequestBody requestFile3 =
                                        RequestBody.create(
                                                MediaType.parse(Objects.requireNonNull(getContentResolver().getType(thirdImageuri))),
                                                file3);
                                body3 = MultipartBody.Part.createFormData("file3", file3.getName(), requestFile3);

                                RequestBody productName = RequestBody.create(MultipartBody.FORM, productname);
                                RequestBody price = RequestBody.create(MultipartBody.FORM, productprice);
                                RequestBody Discount = RequestBody.create(MultipartBody.FORM, discount);
                                RequestBody Brand = RequestBody.create(MultipartBody.FORM, brand);
                                RequestBody Quantity = RequestBody.create(MultipartBody.FORM, quantity);
                                RequestBody Description = RequestBody.create(MultipartBody.FORM, description);
                                RequestBody shopkeeperID = RequestBody.create(MultipartBody.FORM, shopkeeperId);
                                Toast.makeText(AddProductActivity.this, "shopkeeperId22>" + shopkeeperId, Toast.LENGTH_SHORT).show();
                                RequestBody categoryID = RequestBody.create(MultipartBody.FORM, categoryId);

                                Toast.makeText(AddProductActivity.this, "*****" + categoryID + shopkeeperId, Toast.LENGTH_SHORT).show();
                                //next to implement
                                progressDialog = new ProgressDialog(AddProductActivity.this);
                                progressDialog.setTitle("Add Product");
                                progressDialog.setMessage("Please wait while adding product..");
                                progressDialog.show();

                                ProductService.ProductApi productApi = ProductService.getProductApiInstance();
                                //commenting
                                Call<Product> call = productApi.savedProduct(body, body2, body3, categoryID, shopkeeperID, productName, price, Brand, Quantity, Description, Discount);
                                call.enqueue(new Callback<Product>() {
                                    @Override
                                    public void onResponse(Call<Product> call, Response<Product> response) {
                                        if (response.code() == 200) {
                                            Product p = response.body();
                                            Toast.makeText(AddProductActivity.this, "Product added successfully !", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            ab.show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Product> call, Throwable t) {

                                    }
                                });
                            }
                        } else {
                            Toast.makeText(AddProductActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddProductActivity.this, "Invalid input(s)", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddProductActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void chooseBrand(){
        brandList = new ArrayList<>();
        brandList.add("Taparia");
        brandList.add("Hitachi");
        brandList.add("Vega");
        brandList.add("Godrej");
        brandList.add("Tata");
        brandList.add("Cumi Metabo");
        brandList.add("Rolson");
        brandList.add("Dewalt Leatherman");
        brandList.add("Jackly ");
        brandList.add("Europa");
        brandList.add("Aakrati ");
        brandList.add("Jaquar");
        brandList.add("Ceramic");
        brandList.add("Kaymo");
        brandList.add("Cumi");
        brandList.add("Hindware");
        brandList.add("Taisen ");
        brandList.add("Kasta");
        brandList.add("WaterWall");
        brandList.add("Supreme");
        brandList.add("Astral ");
        brandList.add("CPVC");
        brandList.add("Prince");
        brandList.add("German");
        brandList.add("Curved");
        brandList.add("Ketsy");
        brandList.add("DOCOSS");
        brandList.add("SCONNA");
        brandList.add("Alton");
        brandList.add("Arise");
        brandList.add("Quick");
        brandList.add("Zesta");
        brandList.add("Hexagon");
        brandList.add("lavabo");
        brandList.add("ParryWare");
        brandList.add("Sintex");
        brandList.add("Tango");
        brandList.add("Havells ");
        brandList.add("Philips");
        brandList.add("Polycab");

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,brandList);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(binding.ivProductImage1);
            Toast.makeText(this, "" + imageUri, Toast.LENGTH_SHORT).show();
        }
        if (requestCode == 112 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            secondImageUri = data.getData();
            Picasso.get().load(secondImageUri).into(binding.ivProductImage2);
            Toast.makeText(this, "" + secondImageUri, Toast.LENGTH_SHORT).show();
        }
        if (requestCode == 113 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            thirdImageuri = data.getData();
            Picasso.get().load(thirdImageuri).into(binding.ivProductImage3);
            Toast.makeText(this, "" + thirdImageuri, Toast.LENGTH_SHORT).show();
        }
    }

    private void validatingField() {
        awesomeValidation.addValidation(binding.etProductName, "[^\\s*$][a-zA-Z\\s]+", "Enter correct product name");
        awesomeValidation.addValidation(binding.etProductBrand, "[^\\s*$][a-zA-Z\\s]+", "Enter correct Brand name");
        awesomeValidation.addValidation(binding.etProductPrice, "[^\\\\s*$][0-9\\\\s]+", "Enter price");
        awesomeValidation.addValidation(binding.etProductQtyInStock, "[^\\s*$][0-9]*$", "Enter quantitiy");
    }

    private void addProductImage() {
        binding.ivProductImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddProductActivity.this, "Adding first product image", Toast.LENGTH_SHORT).show();
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in, "Select image"), 111);
            }
        });
        binding.ivProductImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in, "Select image"), 112);
            }
        });
        binding.ivProductImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent();
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in, "Select image"), 113);
            }
        });
    }

    private void categorySpinner() {
        adapter = new ArrayAdapter<Category>(AddProductActivity.this, android.R.layout.simple_spinner_dropdown_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setPrompt("Select Category!");
        adapter.notifyDataSetChanged();
        binding.spinnerCategory.setAdapter(adapter);
        binding.spinnerCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isSpinnerTouched = true;
                return false;
            }
        });

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (categoryId == null) {
                    Category c = (Category) binding.spinnerCategory.getSelectedItem();
                    categoryId = c.getCategoryId();
                }
                if (!isSpinnerTouched)//imp method
                    return;
                Category c = (Category) binding.spinnerCategory.getSelectedItem();
                String name = c.getCategoryName();
                Toast.makeText(AddProductActivity.this, "" + name, Toast.LENGTH_SHORT).show();
                categoryId = c.getCategoryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

}
