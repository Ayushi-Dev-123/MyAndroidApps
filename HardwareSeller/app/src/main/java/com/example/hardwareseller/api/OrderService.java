package com.example.hardwareseller.api;

import com.example.hardwareseller.bean.Order;
import com.example.hardwareseller.utility.ServerAddress;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class OrderService {
    public static OrderService.OrderApi orderApi;
    public static OrderService.OrderApi getOrderApiInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000,TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAddress.BASEURL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(orderApi == null)
            orderApi = retrofit.create(OrderService.OrderApi.class);
        return orderApi;
    }

    public interface OrderApi{
        @GET("/order/status/{userId}")
        public Call<ArrayList<Order>> getOrders(@Path("userId") String userId);

        @GET("/order/user/{userId}")
        public Call<ArrayList<Order>> getOrderOfCurrentUser(@Path("userId") String userId);

        @GET("/order/active/{userId}")
        Call<ArrayList<Order>> getActiveOrders(@Path("userId") String userId);

        @GET("/order/cancel/{userId}")
        Call<ArrayList<Order>> getCancelledOrers(@Path("userId") String userId);

        @GET("/order/deliver/{userId}")
        Call<ArrayList<Order>> getDeliveredOrders(@Path("userId") String userId);

        @POST("/order/cancel/{orderId}")
        Call<Order> cancelOrder(@Path("orderId") String orderId);

        @POST("/order/placeOrder")
        Call<Order> placeOrder(@Body Order order);

        @GET("/order/orderId")
        Call<Order> getOrderById(@Path("id") String id);
    }
}
