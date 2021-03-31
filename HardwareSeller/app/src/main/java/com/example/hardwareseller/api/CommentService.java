package com.example.hardwareseller.api;

import com.example.hardwareseller.utility.ServerAddress;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class CommentService {

    public static CommentService.CommentApi commentApi;

    public static CommentService.CommentApi getCommentApiInstance() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAddress.BASEURL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (commentApi == null)
            commentApi = retrofit.create(CommentService.CommentApi.class);
        return commentApi;
    }

    public interface CommentApi {
        @POST("/comment/post")
        Call<Comment> commentProduct(@Body Comment comment);

        @POST("/comment/update/{commentId}")
        Call<Comment> updateComment(@Body Comment comment, @Path("commentId")String commentId);

        @DELETE("/comment/delete/{productId}/{commentId}")
        Call<Comment> deleteComment(@Path("productId") String productId, @Path("commentId") String commentId);

        @GET("/comment/getComment/{productId}")
        Call<ArrayList<com.example.hardwareseller.bean.Comment>> getCommentOfProduct(@Path("productId") String productId);
    }
}
