package com.ayushi.apnachating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.adapter.GroupMessageAdapter;
import com.ayushi.apnachating.adapter.MessageAdapter;
import com.ayushi.apnachating.fragments.ChatMessageButtonSheet;
import com.ayushi.apnachating.fragments.GroupMessageButtomSheet;
import com.ayushi.apnachating.model.Group;
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
import java.util.Iterator;
import java.util.StringJoiner;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView lv;
    TextView tvGroupName, tvGroupDescription;
    ImageView ivSend, ivAttachFile, ivPhoto;
    CircleImageView civGroupIcon;
    EditText etMessage;
    DatabaseReference groupReference, userReference;
    StorageReference storageReference;
    String currentUserId, groupId, currentUserImageUrl;
    ArrayList<Message> messageList;
    GroupMessageAdapter adapter;
    Group group;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__message);

        initComponent();

        groupReference = FirebaseDatabase.getInstance().getReference("Groups");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("GroupImageMessages");
        userReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                   User u = snapshot.getValue(User.class);
                   if(u.getImage()!=null)
                       currentUserImageUrl = u.getImage();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Intent in = getIntent();
        group = (Group) in.getSerializableExtra("group");
        groupId = group.getGroupId();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(GroupChatActivity.this, GroupInfoActivity.class);
                in.putExtra("group",group);
                startActivity(in);
            }
        });

        //getGroupMember();

        Picasso.get().load(group.getIcon()).placeholder(R.drawable.chat_logo2).into(civGroupIcon);
        tvGroupName.setText(group.getGroupName());

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(GroupChatActivity.this);
                pd.setTitle("Sending...");
                pd.setMessage("Please wait...");
                pd.show();

                String data = etMessage.getText().toString();
                if(TextUtils.isEmpty(data)){
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
                String date = sd.format(calendar.getTime());

                sd = new SimpleDateFormat("hh:mm a");
                String time = sd.format(calendar.getTime());

                long timestamp = calendar.getTimeInMillis();
                String messageId = groupReference.push().getKey();
                //String messageId = groupReference.push().getkey();

                Message message = new Message();
                message.setMessageId(messageId);
                message.setFrom(currentUserId);
                message.setDate(date);
                message.setMessage(data);
                message.setType("text");
                message.setTime(time);
                message.setTimestamp(timestamp);
                message.setSenderIcon(currentUserImageUrl);

                groupReference.child(groupId).child("messages").child(messageId).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        if(task.isSuccessful()){
                            etMessage.setText("");
                        }
                        else
                            Toast.makeText(GroupChatActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        groupReference.child(groupId).child("messages").orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    Message msg = snapshot.getValue(Message.class);
                    messageList.add(msg);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                in.setType("image/*");
                startActivityForResult(Intent.createChooser(in,"Select image"),333);
            }
        });

        ivAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupMessageButtomSheet dialog = new GroupMessageButtomSheet(currentUserId,groupId,currentUserImageUrl);
                dialog.show(getSupportFragmentManager(),"send document");
            }
        });
    }//eOf OnCreate
      /*
    private void getGroupMember() {
       groupReference.child(groupId).child("member").addValueEventListener(new ValueEventListener() {
          @RequiresApi(api = Build.VERSION_CODES.N)
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if(dataSnapshot.exists()){
                  Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();
                  StringJoiner sj = new StringJoiner(", ");
                  String memberName = "";
                  while(itr.hasNext()){
                      DataSnapshot snapshot = itr.next();
                      if(!currentUserId.equals(snapshot.getKey())){
                          memberName = snapshot.getValue().toString();
                          sj.add(memberName);
                      }
                  }
                  sj.add("You");
                  tvGroupDescription.setText(""+sj);
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });
    }
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 333 && resultCode == RESULT_OK && data.getClipData() != null ){

            int count = data.getClipData().getItemCount();
            for(int i=0; i< count;i++) {
                final ProgressDialog pd = new ProgressDialog(GroupChatActivity.this);
                pd.setTitle("Sending...");
                pd.setMessage("Please Wait...");
                pd.show();

                Uri imageUri = data.getClipData().getItemAt(i).getUri();

                final String messageId = groupReference.push().getKey();

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

                                final Message msg = new Message();

                                msg.setTimestamp(timestamp);
                                msg.setDate(date);
                                msg.setFrom(currentUserId);
                                msg.setTime(time);
                                msg.setMessageId(messageId);
                                msg.setMessage(imageUrl);
                                msg.setType("image");
                                msg.setSenderIcon(currentUserImageUrl);

                                groupReference.child(groupId).child("messages").child(messageId).setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        pd.dismiss();
                                        if (task.isSuccessful()) {
                                            //Log.e("Image","==>"+imageUrl);
                                            Toast.makeText(GroupChatActivity.this, "Send", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            String errormessage = task.getException().toString();
                                            Toast.makeText(GroupChatActivity.this, errormessage, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        groupReference.child(groupId).child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                    finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initComponent() {
       toolbar  =findViewById(R.id.ChatToolBar);
       civGroupIcon = findViewById(R.id.civFriendProfile);
       tvGroupName = findViewById(R.id.tvFriend);
       tvGroupDescription = findViewById(R.id.tvUserState);
       tvGroupDescription.setText("Tab for more info..");
       etMessage = findViewById(R.id.etChatMessage);
       lv = findViewById(R.id.lvChatMsg);
       ivAttachFile = findViewById(R.id.ivAttachment);
       ivSend = findViewById(R.id.ivSend);
       ivPhoto = findViewById(R.id.ivGallery);

       messageList = new ArrayList<>();
       adapter = new GroupMessageAdapter(GroupChatActivity.this, messageList);
       lv.setAdapter(adapter);
    }
}
