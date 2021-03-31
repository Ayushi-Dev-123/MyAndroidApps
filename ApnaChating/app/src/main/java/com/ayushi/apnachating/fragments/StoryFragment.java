package com.ayushi.apnachating.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.R;
import com.ayushi.apnachating.SendStoryActivity;
import com.ayushi.apnachating.ViewStoryActivity;
import com.ayushi.apnachating.adapter.FriendStoryListAdapter;
import com.ayushi.apnachating.model.Stories;
import com.ayushi.apnachating.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoryFragment extends Fragment {

    FloatingActionButton btnCraeteStory;
    CircleImageView civProfile;
    TextView tvText, tvViewFriendStory;
    RelativeLayout rl;
    DatabaseReference contactReference, userReference, storiesReference;
    User currentUserDetails;
    String currentUserId;
    ArrayList<String> storiesList;
    ArrayList<User> userList;
    RecyclerView rv;
    FriendStoryListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_story,null);
        btnCraeteStory = v.findViewById(R.id.btnCreateStory);
        civProfile = v.findViewById(R.id.civProfile);
        tvText = v.findViewById(R.id.tvText);
        rl = v.findViewById(R.id.rl);
        rv = v.findViewById(R.id.rv);
        tvViewFriendStory = v.findViewById(R.id.tvViewFriendStory);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        storiesReference = FirebaseDatabase.getInstance().getReference("Stories");
        contactReference = FirebaseDatabase.getInstance().getReference("Contacts");

        getCurrentUserDetails();

        checkStories();

       // getUserListOfStory();

        btnCraeteStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .start(getContext(),StoryFragment.this);
            }
        });
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvText.getText().toString().equalsIgnoreCase("View your story")){
                    viewStory(currentUserId);
                }
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        userList = new ArrayList<>();
        adapter = new FriendStoryListAdapter(getContext(),userList);
        adapter.setOnitemClickListner(new FriendStoryListAdapter.OnRecyclerViewClick() {
            @Override
            public void onItemClick(User user, int position) {
                viewStory(user.getUid());
            }
        });
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        getUserListOfStory();
    }

    private void getUserListOfStory() {
        contactReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Iterator<DataSnapshot> itr = snapshot.getChildren().iterator();
                    while(itr.hasNext()){
                        final DataSnapshot ds = itr.next();
                        final String userId = ds.getKey();
                        storiesReference.child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    userReference.child(userId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                User user = snapshot.getValue(User.class);
                                                userList.add(user);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void viewStory(String id) {
       storiesReference.child(id).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
              storiesList = new ArrayList<>();
               Iterator<DataSnapshot> itr = snapshot.getChildren().iterator();
               while(itr.hasNext()){
                   DataSnapshot ds = itr.next();
                   Stories stories = ds.getValue(Stories.class);
                   storiesList.add(stories.getImageUrl());
               }
               Intent in = new Intent(getContext(), ViewStoryActivity.class);
               in.putStringArrayListExtra("storyList",storiesList);
               getContext().startActivity(in);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    private void checkStories(){
        storiesReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    tvText.setText("View your story");
                }
                else{
                    tvText.setText("Create your story");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCurrentUserDetails() {
        userReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    currentUserDetails = snapshot.getValue(User.class);
                    Picasso.get().load(currentUserDetails.getImage()).placeholder(R.drawable.chat_logo2).into(civProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == getActivity().RESULT_OK){
                Uri imageUri = result.getUri();
                Intent in = new Intent(getContext(), SendStoryActivity.class);
                in.putExtra("imageUri","" + imageUri);
                getContext().startActivity(in);
            }
        }
    }
}
