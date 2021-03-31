package com.example.hardwareseller.api;
import com.example.hardwareseller.bean.Category;
import com.example.hardwareseller.utility.ServerAddress;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class CategoryService {
    public static CategoryApi categoryApi;

    public static CategoryApi getCategoryApiInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAddress.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if (categoryApi == null)
            categoryApi = retrofit.create(CategoryApi.class);
        return categoryApi;
    }

    public interface CategoryApi {
        @GET("/category/")
        public Call<ArrayList<Category>> getCategoryList();
    }
}
