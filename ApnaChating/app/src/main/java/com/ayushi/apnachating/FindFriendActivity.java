package com.ayushi.apnachating;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.adapter.UserAdapter;
import com.ayushi.apnachating.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FindFriendActivity extends AppCompatActivity {

    TextView tvFindFreind;
    ImageView ivSearch, ivBackArrow;
    EditText etFindFriend;
    Toolbar toolbar;
    RecyclerView rv;
    String name = "";
    DatabaseReference userReference;
    FirebaseRecyclerAdapter<User,UserAdapter.UserViewHolder> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_findfriend);

        initComponent();

        userReference = FirebaseDatabase.getInstance().getReference("Users");

        etFindFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString();
                onStart();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<User> options = null;
        if(name.equals("")) {
            options = new FirebaseRecyclerOptions.Builder<User>()
                    .setQuery(userReference, User.class)
                    .build();
        }
        else{
            options = new FirebaseRecyclerOptions.Builder<User>()
                    .setQuery(userReference.orderByChild("name").startAt(name).endAt(name+"\uf8ff"),User.class)
                    .build();
        }
        adapter = new UserAdapter(options,FindFriendActivity.this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(FindFriendActivity.this));
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void initComponent(){
        ivBackArrow = findViewById(R.id.ivBackArrow);
        ivSearch = findViewById(R.id.ivSearch);
        etFindFriend = findViewById(R.id.etFindFriend);
        tvFindFreind = findViewById(R.id.tvFindFriend);
        rv = findViewById(R.id.rv);
        toolbar = findViewById(R.id.findfriendToolbar);
        toolbar.setTitle("Find Friend");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               etFindFriend.setVisibility(View.VISIBLE);
               tvFindFreind.setVisibility(View.GONE);
            }
        });
    }
}
