package com.ayushi.apnachating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.adapter.CreateGroupAdapter;
import com.ayushi.apnachating.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateGroupActivity extends AppCompatActivity {

    RecyclerView rv;
    Toolbar toolbar;
    Button btnNext;
    DatabaseReference contactReference, userReference, groupReferencce;
    StorageReference storageReference;
    String currentUserId;
    CreateGroupAdapter adapter;
    CircleImageView civGroupIcon;
    Uri imageUri;
    ArrayList<User> memberList;
    User currentUserDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_create_group);
        initComponent();

        contactReference = FirebaseDatabase.getInstance().getReference("Contacts");
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        groupReferencce = FirebaseDatabase.getInstance().getReference("Groups");
        storageReference = FirebaseStorage.getInstance().getReference("GroupIcons");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getCurrentUserDetails();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                memberList = adapter.getSelectUserList();
                final AlertDialog ab = new AlertDialog.Builder(CreateGroupActivity.this).create();
                View view = LayoutInflater.from(CreateGroupActivity.this).inflate(R.layout.setting_activity, null);
                Toolbar toolbar = view.findViewById(R.id.settingToolbar);
                civGroupIcon = view.findViewById(R.id.circularImageView);
                final EditText etGroupName = view.findViewById(R.id.userName);
                final EditText etGroupDescription = view.findViewById(R.id.status);
                Button btnCreateGroup = view.findViewById(R.id.btnUpdate);

                toolbar.setTitle("Group Information");
                etGroupName.setHint("Group Name");
                etGroupDescription.setText("");
                etGroupDescription.setHint("Group Description");
                btnCreateGroup.setText("Create");
                ab.setView(view);

                civGroupIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .start(CreateGroupActivity.this);
                    }
                });

                btnCreateGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog pd = new ProgressDialog(CreateGroupActivity.this);
                        pd.setTitle("Creating...");
                        pd.setMessage("Please Wait...");
                        pd.show();

                        if(imageUri != null){
                            final String groupName = etGroupName.getText().toString();
                            if(TextUtils.isEmpty(groupName)){
                                etGroupName.setError("enter group name first");
                                return;
                            }
                            final String description = etGroupDescription.getText().toString();
                            if(TextUtils.isEmpty(description)){
                                etGroupName.setError("Required!");
                                return;
                            }
                            final String groupId = groupReferencce.push().getKey();
                            StorageReference filePath = storageReference.child(groupId+".jpg");

                            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                   taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           String imageUrl = uri.toString();

                                           groupReferencce.child(groupId).child(currentUserId).setValue("");

                                           for(User u : memberList){
                                               groupReferencce.child(groupId).child("member").child(u.getUid()).setValue(u.getName());
                                               groupReferencce.child(groupId).child(u.getUid()).setValue("");
                                           }
                                           groupReferencce.child(groupId).child("member").child(currentUserId).setValue(currentUserDetails.getName());

                                           Calendar calendar = Calendar.getInstance();
                                           SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                                           String createdAt = sd.format(calendar.getTime());

                                           HashMap<String, Object> hm = new HashMap<>();
                                           hm.put("groupName",groupName);
                                           hm.put("description",description);
                                           hm.put("createdAt",createdAt);
                                           hm.put("createdBy",currentUserId);
                                           hm.put("icon",imageUrl);
                                           hm.put("groupId",groupId);

                                           groupReferencce.child(groupId).updateChildren(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   pd.dismiss();
                                                   if(task.isSuccessful()){
                                                       ab.dismiss();
                                                       Toast.makeText(CreateGroupActivity.this, "Group Created", Toast.LENGTH_SHORT).show();
                                                       finish();
                                                   }
                                                   else {
                                                       Toast.makeText(CreateGroupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           });
                                       }
                                   });
                                }
                            });
                        }
                    }
                });
                ab.show();
            }
        });
    }

    private void getCurrentUserDetails() {
    userReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
           if(snapshot.exists()){
               currentUserDetails = snapshot.getValue(User.class);
           }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setIndexedQuery(contactReference.child(currentUserId),userReference, User.class)
                .build();

        adapter = new CreateGroupAdapter(options);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(CreateGroupActivity.this));
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void initComponent() {
        rv = findViewById(R.id.rvCreateGroup);
        toolbar = findViewById(R.id.createGroupToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnNext = findViewById(R.id.btnNext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                imageUri = result.getUri();
                civGroupIcon.setImageURI(imageUri);
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){

            }
        }
    }
}
