package com.ayushi.apnachating;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.adapter.CreateGroupAdapter;
import com.ayushi.apnachating.adapter.GroupMemberMessageAdapter;
import com.ayushi.apnachating.adapter.ShowGroupMemberAdapter;
import com.ayushi.apnachating.model.Group;
import com.ayushi.apnachating.model.Message;
import com.ayushi.apnachating.model.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Iterator;

public class GroupInfoActivity extends AppCompatActivity {

    RecyclerView rv;
    ImageView ivGroupIcon;
    FloatingActionButton btnExitGroup, btnEdit, btnAddMember;
    Toolbar toolbar;
    Group group;
    DatabaseReference memberReference, messageReference, userReference, groupReference;
    ShowGroupMemberAdapter adapter;
    String currentUserId;
    ArrayList<Message> memberMessageList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        Intent in = getIntent();
        group = (Group)in.getSerializableExtra("group");

        initComponent();

        userReference = FirebaseDatabase.getInstance().getReference("Users");
        memberReference = FirebaseDatabase.getInstance().getReference("Groups").child(group.getGroupId()).child("member");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        groupReference = FirebaseDatabase.getInstance().getReference("Groups");

        if(currentUserId.equals(group.getCreatedBy())){
            removeMember();
            btnAddMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent in = new Intent(GroupInfoActivity.this, AddMemberIntoGroupActivity.class);
                   in.putExtra("group",group);
                   startActivity(in);
                }
            });
        }
        else {
            btnAddMember.setVisibility(View.GONE);
        }

        btnExitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUserId.equals(group.getCreatedBy())){
                    Intent in = new Intent(GroupInfoActivity.this, MakeAdminActivity.class);
                    in.putExtra("group",group);
                    startActivity(in);
                }
                else{
                    exitGroup();
                }
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(GroupInfoActivity.this);
            }
        });
    }

    private void exitGroup() {
        AlertDialog.Builder ab = new AlertDialog.Builder(GroupInfoActivity.this);
        ab.setTitle("Alert");
        ab.setMessage("Do you want to exit group ?");
        ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                final ProgressDialog pd = new ProgressDialog(GroupInfoActivity.this);
                pd.setTitle("Please wait...");
                pd.show();
                memberReference.child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        groupReference.child(group.getGroupId()).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                pd.dismiss();
                                if(task.isSuccessful()){
                                   finish();
                                }
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

    private void removeMember() {
        new ItemTouchHelper(new DeleteOnSwipe(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT))
                .attachToRecyclerView(rv);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setIndexedQuery(memberReference,userReference, User.class)
                .build();

        adapter = new ShowGroupMemberAdapter(options);

        adapter.setOnItemClick(new ShowGroupMemberAdapter.OnRecyclerViewClick() {
            @Override
            public void onItemClick(final User user, int position) {
                messageReference = FirebaseDatabase.getInstance().getReference("Groups").child(group.getGroupId()).child("messages");
                messageReference.orderByChild("from").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if((snapshot.exists())) {
                           memberMessageList = new ArrayList<>();
                           Iterator<DataSnapshot> itr = snapshot.getChildren().iterator();
                           while (itr.hasNext()) {
                               DataSnapshot ds = itr.next();
                               Message msg = ds.getValue(Message.class);
                               memberMessageList.add(msg);
                           }
                           showMessageInListView(memberMessageList,user);
                       }
                       else {
                           //Toast.makeText(GroupInfoActivity.this, , Toast.LENGTH_SHORT).show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }
        });
        rv.setAdapter(adapter);
        //rv.setLayoutManager(new LinearLayoutManager(GroupInfoActivity.this));
        adapter.startListening();
    }

    private void showMessageInListView(ArrayList<Message> memberMessageList, User user){
        final AlertDialog ab = new AlertDialog.Builder(GroupInfoActivity.this).create();
        View v = LayoutInflater.from(GroupInfoActivity.this).inflate(R.layout.layout_list_view,null);
        TextView tvName = v.findViewById(R.id.tvName);
        ImageView ivClose = v.findViewById(R.id.ivClose);
        ListView lv = v.findViewById(R.id.lv);
        GroupMemberMessageAdapter adapter = new GroupMemberMessageAdapter(GroupInfoActivity.this,memberMessageList);
        lv.setAdapter(adapter);
        tvName.setText("Message send by " + user.getName());
        ab.setView(v);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ab.dismiss();
            }
        });
        ab.show();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        groupReference.child(group.getGroupId()).child(currentUserId).addValueEventListener(new ValueEventListener() {
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

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    class DeleteOnSwipe extends ItemTouchHelper.SimpleCallback{

        public DeleteOnSwipe(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
           int position = viewHolder.getAdapterPosition();
           final User user = adapter.getItem(position);
           memberReference.child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   groupReference.child(group.getGroupId()).child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Snackbar.make(rv,"Member removed",Snackbar.LENGTH_LONG)
                                       .setActionTextColor(Color.GREEN)
                                       .setAction("Undo", new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               memberReference.child(user.getUid()).setValue(user.getName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {
                                                      groupReference.child(group.getGroupId()).child(user.getUid()).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                          @Override
                                                          public void onComplete(@NonNull Task<Void> task) {
                                                             if(task.isSuccessful()){
                                                                 Snackbar.make(rv,"Member added",Snackbar.LENGTH_LONG).show();
                                                             }
                                                             else
                                                                 Toast.makeText(GroupInfoActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                          }
                                                      });
                                                   }
                                               });
                                           }
                               }).show();
                           }
                       }
                   });
               }
           });

        }
    }

    private void initComponent() {
        rv = findViewById(R.id.rvGroupInfo);
        btnEdit = findViewById(R.id.EditGroupIcon);
        ivGroupIcon = findViewById(R.id.ivGroupIcon);
        btnExitGroup = findViewById(R.id.btnExitGroup);
        btnAddMember  =findViewById(R.id.btnAddMember);
        toolbar = findViewById(R.id.GroupInfoToolbar);

        toolbar.setTitle(group.getGroupName());
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.get().load(group.getIcon()).placeholder(R.drawable.chat_logo2).into(ivGroupIcon);
    }
}
