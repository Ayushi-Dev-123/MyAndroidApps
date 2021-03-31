package com.example.json2images;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.json2images.Author.Author;
import com.example.json2images.adapter.AuthorAdaptor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    RecyclerView.Adapter<AuthorAdaptor.AuthorViewHolder>adapter;
    RecyclerView.LayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv =  findViewById(R.id.rv);
        String api = "https://picsum.photos/v2/list";
        StringRequest request = new StringRequest(Request.Method.GET, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<Author> authorList = new ArrayList<>();
                    JSONArray arr = new JSONArray(response);
                    for(int i=0;i<arr.length();i++){
                        JSONObject obj = arr.getJSONObject(i);
                       String id =  obj.getString("id");
                       String author = obj.getString("author");
                       int width = obj.getInt("width");
                       int height = obj.getInt("height");
                       String download_url = obj.getString("download_url");

                       Author auth = new Author(id,author,download_url,width,height);
                       authorList.add(auth);
                    }
                    adapter = new AuthorAdaptor(authorList);
                    manager = new LinearLayoutManager(MainActivity.this);
                    rv.setAdapter(adapter);
                    rv.setLayoutManager(manager);

                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Toast.makeText(MainActivity.this,""+error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(request);
    }
}