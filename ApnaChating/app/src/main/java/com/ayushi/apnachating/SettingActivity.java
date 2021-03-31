package com.ayushi.apnachating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ayushi.apnachating.model.User;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    ProgressDialog pd;
    EditText etUserName, etStatus;
    CircleImageView imageView;
    Button updateStatus;
    View view;
    FirebaseAuth mAuth;
    DatabaseReference rootReference;
    String currentUserId;
    StorageReference storageReference;
    Toolbar toolbar;
    User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        initComponent();

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        rootReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Images");

        reteriveUserStatus();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(SettingActivity.this);
            }
        });

        updateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(SettingActivity.this);
                pd.setTitle("Updating Status...");
                pd.setMessage("Please Wait...");
                pd.show();

                String name = etUserName.getText().toString();
                String status = etStatus.getText().toString();

                if(TextUtils.isEmpty(name)){
                    etUserName.setError("Username Required");
                    return;
                }
               if(TextUtils.isEmpty(status)){
                   status = "Hi, i am using ApnaChatting";
               }

               HashMap<String,String> hm = new HashMap<>();
               hm.put("name",name);
               hm.put("status",status);
               hm.put("uid",currentUserId);
               if(user!=null){
                   String imageUrl = user.getImage();
                   if(imageUrl!=null){
                       hm.put("image",imageUrl);
                   }
               }
               rootReference.child("Users").child(currentUserId).setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       pd.dismiss();
                       if (task.isSuccessful()) {
                           Toast.makeText(SettingActivity.this, "Status Updated", Toast.LENGTH_SHORT).show();
                           sendUserToMainActivity();
                           //finish();
                       }
                       else{
                           String message = task.getException().toString();
                           Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
                       }
                   }
               });

            }
        });
    }

    private void sendUserToMainActivity() {
        Intent mainIntent = new Intent(SettingActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void reteriveUserStatus() {
        rootReference.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Datasnap Shot","" + dataSnapshot);
                if(dataSnapshot.exists()){
                    user = dataSnapshot.getValue(User.class);
                    etUserName.setText(user.getName());
                    etStatus.setText(user.getStatus());
                    //if(user.getImage()!=null)
                       Picasso.get().load(user.getImage()).placeholder(R.drawable.chat_logo2).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result  = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK) {
               final Uri croppedImageUri = result.getUri();
               StorageReference filePath = storageReference.child(currentUserId + ".jpg");
               filePath.putFile(croppedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {
                             String imageUrl = uri.toString();
                             rootReference.child("Users").child(currentUserId).child("image").setValue(imageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SettingActivity.this, "Profile Uploaded", Toast.LENGTH_SHORT).show();
                                        imageView.setImageURI(croppedImageUri);
                                    }
                                    else{
                                        String message = task.getException().toString();
                                        Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                 }
                             });
                          }
                      });
                   }
               });
            }
        }
    }

    private void initComponent() {
        toolbar = findViewById(R.id.settingToolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etStatus = findViewById(R.id.status);
        etUserName = findViewById(R.id.userName);
        imageView = findViewById(R.id.circularImageView);
        view = findViewById(R.id.view);
        updateStatus = findViewById(R.id.btnUpdate);
    }
}
