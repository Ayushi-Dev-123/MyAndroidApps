package com.ayushi.apnachating.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.R;
import com.ayushi.apnachating.adapter.ChatRequestAdapter;
import com.ayushi.apnachating.model.ChatRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestFragment extends Fragment {
    RecyclerView rvRequest;
    DatabaseReference chatRequestReference;
    String currentUserId;
    FirebaseAuth mAuth;
    FirebaseRecyclerAdapter<ChatRequest, ChatRequestAdapter.ChatRequestViewHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.request_fragment,null);
        rvRequest = v.findViewById(R.id.rvRequest);
        chatRequestReference = FirebaseDatabase.getInstance().getReference("ChatRequest");
        //currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUserId = mAuth.getInstance().getCurrentUser().getUid();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ChatRequest> options = new FirebaseRecyclerOptions.Builder<ChatRequest>()
                .setQuery(chatRequestReference.child(currentUserId).orderByChild("request_type").equalTo("receive"),ChatRequest.class)
                .build();

        adapter = new ChatRequestAdapter(options,getContext());
        rvRequest.setAdapter(adapter);
        rvRequest.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}




