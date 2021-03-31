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
import com.ayushi.apnachating.adapter.ContactAdapter;
import com.ayushi.apnachating.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactFragment extends Fragment {
    RecyclerView rv;
    DatabaseReference conatactReference, userReference;
    FirebaseRecyclerAdapter<User, ContactAdapter.ContactViewHolder> adapter;
    String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_fragment, null);
        rv = v.findViewById(R.id.rvContact);
        conatactReference = FirebaseDatabase.getInstance().getReference("Contacts");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<User>options = new FirebaseRecyclerOptions.Builder<User>()
                .setIndexedQuery(conatactReference.child(currentUserId),userReference,User.class)
                .build();

        adapter = new ContactAdapter(options,getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
