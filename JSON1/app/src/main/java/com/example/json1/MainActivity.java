package com.example.json1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.json1.adapter.PostAdapter;
import com.example.json1.model.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnCall;
    RecyclerView rv;
    RecyclerView.Adapter<PostAdapter.PostViewHolder>adapter;
    RecyclerView.LayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCall = findViewById(R.id.btnclick);
        rv = findViewById(R.id.rv);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String api = "https://jsonplaceholder.typicode.com/posts";
                StringRequest request = new StringRequest(Request.Method.GET, api, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
                        //Log.e("Response ==> ", "==>" + response);
                        try{
                            ArrayList<Post> postList = new ArrayList<>();
                            JSONArray arr = new JSONArray(response);
                            for(int i=0; i<arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);

                                int userId = obj.getInt("userId");
                                int id = obj.getInt("id");
                                String title = obj.getString("title");
                                String body = obj.getString("body");

                                Post p = new Post(userId,id,title,body);
                                postList.add(p);
                            }
                            adapter = new PostAdapter(postList);
                            manager = new LinearLayoutManager(MainActivity.this);
                            rv.setAdapter(adapter);
                            rv.setLayoutManager(manager);
                        }
                        catch (Exception e){

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, ""+error,Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(request);
            }
        });
    }
}