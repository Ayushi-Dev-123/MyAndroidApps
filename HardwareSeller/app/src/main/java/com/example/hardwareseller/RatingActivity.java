package com.example.hardwareseller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.hardwareseller.adapter.ShowCommentAdapter;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity {
    com.example.hardwareseller.databinding.ShowReviewsActivityBinding binding;
    ShowCommentAdapter adapter;
    ArrayList<Comment> comments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.hardwareseller.databinding.ShowReviewsActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        comments = (ArrayList<Comment>) in.getSerializableExtra("commentList");

        for(Comment comment : comments) {
            adapter = new ShowCommentAdapter(RatingActivity.this,comments);
            binding.rvComment.setLayoutManager(new LinearLayoutManager(RatingActivity.this));
            binding.rvComment.setAdapter(adapter);
        }
    }
}