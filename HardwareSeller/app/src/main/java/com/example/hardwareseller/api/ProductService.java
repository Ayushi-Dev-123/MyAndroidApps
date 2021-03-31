package com.example.hardwareseller.api;

import com.example.hardwareseller.bean.Product;
import com.example.hardwareseller.utility.ServerAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ProductService {
    public static ProductApi productApi;

    public static ProductApi getProductApiInstance() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAddress.BASEURL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if (productApi == null)
            productApi = retrofit.create(ProductApi.class);
        return productApi;
    }

    public interface ProductApi {
        @Multipart
        @POST("/product/")
        public Call<Product> saveProduct(@Part MultipartBody.Part file,
                                         @Part("categoryId") RequestBody categoryId,
                                         @Part("shopkeeperId") RequestBody shopkeeperId,
                                         @Part("name") RequestBody name,
                                         @Part("price") RequestBody price,
                                         @Part("brand") RequestBody brand,
                                         @Part("qtyInStock") RequestBody qtyInStock,
                                         @Part("description") RequestBody description,
                                         @Part("discount") RequestBody discount);

        @Multipart
        @POST("/product/save")
        public Call<Product> savedProduct(@Part MultipartBody.Part file,
                                          @Part MultipartBody.Part file2,
                                          @Part MultipartBody.Part file3,
                                          @Part("categoryId") RequestBody categoryId,
                                          @Part("shopkeeperId") RequestBody shopkeeperId,
                                          @Part("name") RequestBody name,
                                          @Part("price") RequestBody price,
                                          @Part("brand") RequestBody brand,
                                          @Part("qtyInStock") RequestBody qtyInStock,
                                          @Part("description") RequestBody description,
                                          @Part("discount") RequestBody discount);


        @GET("/product/productlist/{categoryId}/{shopkeeperId}")
        public Call<List<Product>> getProductByCategoryAndShopKeeper(@Path("categoryId") String categoryId,
                                                                     @Path("shopkeeperId") String shopkeeperId);

        @GET("/product/name/{name}")
        public Call<ArrayList<Product>> searchProductByName(@Path("name") String name);

    }
}
