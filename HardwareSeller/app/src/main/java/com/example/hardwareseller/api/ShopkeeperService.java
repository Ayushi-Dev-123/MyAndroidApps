package com.example.hardwareseller.api;

import com.example.hardwareseller.bean.Shopkeeper;
import com.example.hardwareseller.utility.ServerAddress;

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

public class ShopkeeperService {

    public static ShopkeeperApi shopkeeperApi ;

    public static ShopkeeperApi getStoreApiInstance() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAddress.BASEURL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (shopkeeperApi == null)
            shopkeeperApi = retrofit.create(ShopkeeperApi.class);
        return shopkeeperApi;
    }

    public interface ShopkeeperApi {
        @Multipart
        @POST("/shopkeeper/")
        public Call<Shopkeeper> saveStoreProfile(@Part MultipartBody.Part file,
                                                 @Part("name") RequestBody name,
                                                 @Part("shopName") RequestBody shopName,
                                                 @Part("contactNumber") RequestBody contactNumber,
                                                 @Part("address") RequestBody address,
                                                 @Part("email") RequestBody email,
                                                 @Part("token") RequestBody token,
                                                 @Part("shopkeeperId")RequestBody shopkeeperId);

        @GET("/shopkeeper/view/{id}")
        public Call<Shopkeeper> getShopKeeperProfile(@Path("id") String id);
    }
}
