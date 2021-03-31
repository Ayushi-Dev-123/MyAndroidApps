package com.ayushi.apnachating.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.GroupChatActivity;
import com.ayushi.apnachating.R;
import com.ayushi.apnachating.adapter.ShowGroupAdapter;
import com.ayushi.apnachating.model.Group;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GroupFragment extends Fragment {

    RecyclerView rv;
    DatabaseReference groupReference;
    String currentUser;
    ShowGroupAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.group_fragment, null);
        rv = v.findViewById(R.id.rvGroups);

        groupReference = FirebaseDatabase.getInstance().getReference("Groups");
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseRecyclerOptions<Group> options = new FirebaseRecyclerOptions.Builder<Group>()
                .setQuery(groupReference.orderByChild(currentUser).equalTo(""),Group.class)
                .build();

        adapter = new ShowGroupAdapter(options,getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setOnItemClicklistner(new ShowGroupAdapter.OnRecyclerViewClick() {
            @Override
            public void onItemClick(Group group, int position) {
                Intent in = new Intent(getContext(), GroupChatActivity.class);
                in.putExtra("group",group);
                startActivity(in);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
