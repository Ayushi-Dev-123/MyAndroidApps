package com.example.retrofittest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class ProductApi {
    private static final String baseUrl = "http://192.168.43.148:8080/";
    private static ProductService productService = null;

    public static ProductService getProductService(){
        if(productService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            productService = retrofit.create(ProductService.class);
        }
        return productService;
    }

    public interface ProductService{
       @GET("product-list")
       Call<List<Product>> getProductList();

       @POST("save")
        Call<Product> saveProduct(@Body Product p);

       @GET("product/{id}")
        Call<Product> getProduct(@Path("id") int id);
    }

}
