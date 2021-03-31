package com.ayushi.apnachating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ayushi.apnachating.model.Stories;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SendStoryActivity extends AppCompatActivity {

    ImageView ivStory;
    EditText etText;
    FloatingActionButton btnSend;
    String currentUserId;
    StorageReference storageReference;
    DatabaseReference storiesReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_story);

        initComponent();

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference("Stories");
        storiesReference = FirebaseDatabase.getInstance().getReference("Stories");

        Intent in = getIntent();
        final Uri imageUri = Uri.parse(in.getStringExtra("imageUri"));
        ivStory.setImageURI(imageUri);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(SendStoryActivity.this);
                pd.setTitle("Please wait..");
                pd.setMessage("while creating story");
                pd.show();

                final String storyId = storiesReference.push().getKey();
                StorageReference filePath = storageReference.child(storyId+".jpg");
                filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                String text = etText.getText().toString();
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
                                String date = sd.format(calendar.getTime());
                                sd  = new SimpleDateFormat("hh:mm a");
                                String time = sd.format(calendar.getTime());
                                long timeStamp = calendar.getTimeInMillis();

                                Stories story = new Stories();
                                story.setDate(date);
                                story.setTime(time);
                                story.setImageUrl(imageUrl);
                                story.setStoryId(storyId);
                                if(TextUtils.isEmpty(text)){
                                   text = "";
                                }
                                story.setText(text);
                                story.setTimeStamp(timeStamp);
                                story.setUid(currentUserId);
                                story.setType("image");

                                storiesReference.child(currentUserId).child(storyId).setValue(story).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        pd.dismiss();
                                        if(task.isSuccessful()){
                                           Toast.makeText(SendStoryActivity.this, "Story created sussessfully", Toast.LENGTH_SHORT).show();
                                           finish();
                                        }
                                       else
                                           Toast.makeText(SendStoryActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        });
                    }
                });
            }
        });


    }

    private void initComponent() {
       ivStory = findViewById(R.id.ivStory);
       btnSend = findViewById(R.id.btnSend);
       etText = findViewById(R.id.etCaption);
    }
}
