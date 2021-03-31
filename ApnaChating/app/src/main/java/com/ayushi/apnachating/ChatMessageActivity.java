package com.ayushi.apnachating;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.adapter.MessageAdapter;
import com.ayushi.apnachating.fragments.ChatMessageButtonSheet;
import com.ayushi.apnachating.model.Message;
import com.ayushi.apnachating.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageActivity extends AppCompatActivity {

    ListView lv;
    Toolbar toolbar;
    TextView tvFriendName, userState;
    EditText etmsg;
    CircleImageView currentFriendPhoto;
    ImageView ivSend, ivAttachment, ivPhoto;
    DatabaseReference contactReference, messageReference, messageCounterReference;
    String currentUserId, receiverUserId;
    ArrayList<Message> messageList;
    ArrayAdapter<Message> adapter;
    StorageReference storageReference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__message);
        initComponent();
        Intent in = getIntent();
        User user = (User)in.getSerializableExtra("friend");
        Picasso.get().load(user.getImage()).placeholder(R.drawable.chat_logo2).into(currentFriendPhoto);
        tvFriendName.setText(user.getName());
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverUserId = user.getUid();
        messageReference = FirebaseDatabase.getInstance().getReference("Messages");
        storageReference = FirebaseStorage.getInstance().getReference("ImageMessages");
        messageCounterReference = FirebaseDatabase.getInstance().getReference("MessageCounter");
        contactReference = FirebaseDatabase.getInstance().getReference("Contacts");

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etmsg.getText().toString();
                if(TextUtils.isEmpty(message)){
                    return;
                }
                final String messageId = messageReference.push().getKey();
                Calendar cdate = Calendar.getInstance();
                SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
                String date = sd.format(cdate.getTime());

                Calendar ctime = Calendar.getInstance();
                sd = new SimpleDateFormat("hh:mm a");
                String time = sd.format(ctime.getTime());

                long timestamp = Calendar.getInstance().getTimeInMillis();

                final Message msg = new Message();
                msg.setDate(date);
                msg.setTime(time);
                msg.setTimestamp(timestamp);
                msg.setTo(receiverUserId);
                msg.setFrom(currentUserId);
                msg.setType("text");
                msg.setMessage(message);
                msg.setMessageId(messageId);

                messageReference.child(currentUserId).child(receiverUserId).child(messageId).setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            messageReference.child(receiverUserId).child(currentUserId).child(messageId).setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        etmsg.setText("");
                                        messageCounterReference.child(receiverUserId).child(currentUserId).child(messageId).setValue("");
                                    }
                                    else {
                                        String errorMessage = task.getException().toString();
                                        Toast.makeText(ChatMessageActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                        etmsg.setText("");
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        messageReference.child(currentUserId).child(receiverUserId).orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               if(dataSnapshot.exists()){
                   Message msg = dataSnapshot.getValue(Message.class);
                   messageList.add(msg);
                   adapter.notifyDataSetChanged();
               }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                in.setType("image/*");
                startActivityForResult(Intent.createChooser(in,"Select image"),111);
            }
        });

        ivAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMessageButtonSheet dialog = new ChatMessageButtonSheet(currentUserId,receiverUserId);
                dialog.show(getSupportFragmentManager(),"send document");
            }
        });
    } //eOf OnCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 111 && resultCode == RESULT_OK && data.getClipData() != null ){

            int count = data.getClipData().getItemCount();
            for(int i=0; i< count;i++) {
               final ProgressDialog pd = new ProgressDialog(ChatMessageActivity.this);
               pd.setTitle("Sending...");
               pd.setMessage("Please Wait...");
               pd.show();

               Uri imageUri = data.getClipData().getItemAt(i).getUri();

               final String messageId = messageReference.push().getKey();

               StorageReference filePath = storageReference.child(messageId + ".jpg");
               filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String imageUrl = uri.toString();
                                Calendar cdate = Calendar.getInstance();
                                SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
                                String date = sd.format(cdate.getTime());
                                Calendar ctime = Calendar.getInstance();
                                sd = new SimpleDateFormat("hh:mm a");
                                String time = sd.format(ctime.getTime());

                                long timestamp = Calendar.getInstance().getTimeInMillis();

                                final Message msg = new Message(date, time, "image", currentUserId, receiverUserId, messageId, imageUrl, timestamp);

                                messageReference.child(currentUserId).child(receiverUserId).child(messageId).setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        messageReference.child(receiverUserId).child(currentUserId).child(messageId).setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                pd.dismiss();
                                                if (task.isSuccessful()) {
                                                    //Log.e("Image","==>"+imageUrl);
                                                    Toast.makeText(ChatMessageActivity.this, "Send", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    String errormessage = task.getException().toString();
                                                    Toast.makeText(ChatMessageActivity.this, errormessage, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Users");
        usersReference.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    if(user.getState() != null){
                        if(user.getState().equals("Online")){
                            userState.setText("Online");
                        }
                        else if(user.getState().equals("Offline")){
                            userState.setText("Last Seen : "+ user.getDate() + " "+user.getTime());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }//eOf onStart

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Remove Friend");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        if(title.equalsIgnoreCase("Remove Friend"));{
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle("Alert ! ");
            ab.setMessage("If you remove then chat will authomatically delete of this user ");
            ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ProgressDialog pd = new ProgressDialog(ChatMessageActivity.this);
                    pd.setTitle("Please wait..");
                    pd.setMessage("while removing user from chat");
                    pd.show();
                    contactReference.child(currentUserId).child(receiverUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            contactReference.child(receiverUserId).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    pd.dismiss();
                                    if(task.isSuccessful()){
                                        finish();
                                        //Toast.makeText(ChatMessageActivity.this, "", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(ChatMessageActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
            ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ab.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initComponent() {

        toolbar = findViewById(R.id.ChatToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentFriendPhoto = findViewById(R.id.civFriendProfile);
        etmsg = findViewById(R.id.etChatMessage);
        tvFriendName = findViewById(R.id.tvFriend);
        userState = findViewById(R.id.tvUserState);
        lv = findViewById(R.id.lvChatMsg);
        ivAttachment =findViewById(R.id.ivAttachment);
        ivPhoto = findViewById(R.id.ivGallery);
        ivSend = findViewById(R.id.ivSend);
        messageList = new ArrayList<>();
        adapter = new MessageAdapter(ChatMessageActivity.this, messageList);
        lv.setAdapter(adapter);
    }
}
