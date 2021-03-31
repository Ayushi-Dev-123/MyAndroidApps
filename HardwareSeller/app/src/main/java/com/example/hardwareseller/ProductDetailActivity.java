package com.example.hardwareseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwareseller.adapter.SliderAdapterExample;
import com.example.hardwareseller.api.CommentService;
import com.example.hardwareseller.bean.Comment;
import com.example.hardwareseller.bean.Product;
import com.example.hardwareseller.bean.SliderItem;
import com.example.hardwareseller.databinding.ActivityProductDetailBinding;
import com.example.hardwareseller.utility.InternetConnectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    //ProductDetailActivityBinding binding;
    ActivityProductDetailBinding binding;
    Product product;
    InternetConnectivity connectivity;
    String currentUserId, productId,brand,description,imageUrl,shopkeeperId, categoryId, name;
    ArrayList<Product> productList;
    SliderAdapterExample sliderAdapterExample;
    Float rate, avgRate = 0f, avg;
    double price;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ProductDetailActivityBinding.inflate(LayoutInflater.from(ProductDetailActivity.this));
        binding = ActivityProductDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent in = getIntent();
        product = (Product) in.getSerializableExtra("productDescription");
        productId = product.getProductId();
        setProductData();
        viewRating();
        productData();
    }

    private void setProductData() {
        SliderAdapterExample sliderAdapterExample = new SliderAdapterExample(this);
        binding.iv.setSliderAdapter(sliderAdapterExample);
        binding.iv.setIndicatorAnimation(IndicatorAnimationType.WORM);
        //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.iv.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.iv.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.iv.setIndicatorSelectedColor(Color.YELLOW);
        binding.iv.setIndicatorMargin(1);
        binding.iv.setIndicatorUnselectedColor(Color.GRAY);
        binding.iv.setScrollTimeInSec(2);
        binding.iv.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {

            }
        });
        renewItems(binding.getRoot());

        binding.tvProductName.setText("Name: " + product.getName());
        binding.tvDiscountedPrice.setText("Discount:% " + product.getDiscount());
        binding.tvQuantity.setText("Quantity: " + product.getQtyInStock());
        binding.tvProductDescription.setText("Description: " + product.getDescription());
        binding.tvProductPrice.setPaintFlags(binding.tvProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.tvProductPrice.setText("Price:₹ " + product.getPrice());
    }

    private void viewRating() {
        if (connectivity.isConnectedToInternet(this)) {
            CommentService.CommentApi commentApi = CommentService.getCommentApiInstance();
            Call<ArrayList<Comment>> call = commentApi.getCommentOfProduct(productId);
            call.enqueue(new Callback<ArrayList<Comment>>() {
                @Override
                public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                    if (response.code() == 200) {
                        final ArrayList<Comment> commentList = response.body();
                        if (commentList.size() == 0) {
                            binding.tvRating.setVisibility(View.GONE);
                            binding.rl.setVisibility(View.GONE);
                        }
                        calculateAverageRating(commentList);

                        binding.tvViewReview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent in = new Intent(ProductDetailActivity.this, RatingActivity.class);
                                in.putExtra("commentList", commentList);
                                startActivity(in);
                            }
                        });
                    } else
                        Toast.makeText(ProductDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {

                }
            });
        } else
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
    }

    private void calculateAverageRating(ArrayList<Comment> list) {
        Long average, user1 = 0L, user2 = 0L, user3 = 0L, user4 = 0L, user5 = 0L;
        for (Comment comment : list) {
            if (comment.getRating() == 5) {
                user5++;
            }
            if (comment.getRating() == 4) {
                user4++;
            }
            if (comment.getRating() == 3) {
                user3++;
            }
            if (comment.getRating() == 2) {
                user2++;
            }
            if (comment.getRating() == 1) {
                user1++;
            }
            average = ((user1 * 1) + (user2 * 2) + (user3 * 3) + (user4 * 4) + (user5 * 5)) / (user1 + user2 + user3 + user4 + user5);
            binding.ratingBar.setRating(average);
        }
    }

    private void productData() {
        productId = product.getProductId();
        name = product.getName();
        brand = product.getBrand();
        categoryId = product.getCategoryId();
        price = product.getPrice();
        shopkeeperId = product.getShopkeeperId();
        Log.e("Shop", "==>" + shopkeeperId);
        imageUrl = product.getImageUrl();
        description = product.getDescription();
    }


    public void renewItems(View view) {
        List<SliderItem> sliderItemList = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            SliderItem sliderItem = new SliderItem();
            if (i == 1) {
                sliderItem.setImageUrl(product.getImageUrl());
            } else if (i == 2) {
                sliderItem.setImageUrl(product.getSecondImageUrl());
            } else if (i == 3) {
                sliderItem.setImageUrl(product.getThirdImageurl());
            }
            sliderItemList.add(sliderItem);
        }
        sliderAdapterExample.renewItems(sliderItemList);
    }
}
