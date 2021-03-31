package com.ayushi.apnachating.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.R;
import com.ayushi.apnachating.model.ChatRequest;
import com.ayushi.apnachating.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRequestAdapter extends FirebaseRecyclerAdapter<ChatRequest, ChatRequestAdapter.ChatRequestViewHolder> {

    Context context;
    DatabaseReference userReference, contactReference, chatRequestReference;
    String currentUsserId;

    public ChatRequestAdapter(@NonNull FirebaseRecyclerOptions<ChatRequest> options, Context context) {
        super(options);
        this.context = context;
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        contactReference = FirebaseDatabase.getInstance().getReference("Contacts");
        chatRequestReference = FirebaseDatabase.getInstance().getReference("ChatRequest");
        currentUsserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ChatRequestViewHolder holder, int i, @NonNull final ChatRequest chatRequest) {
        final String sendUserId = getRef(i).getKey();
        userReference.child(sendUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User  user = dataSnapshot.getValue(User.class);
                    holder.tvStatus.setText(user.getStatus());
                    holder.tvName.setText(user.getName());
                    Picasso.get().load(user.getImage()).placeholder(R.drawable.chat_logo2).into(holder.civProfile);

                    holder.btnAcceptRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            contactReference.child(currentUsserId).child(sendUserId).child("contact").setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        contactReference.child(sendUserId).child(currentUsserId).child("contact").setValue("saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    chatRequestReference.child(currentUsserId).child(sendUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                           if(task.isSuccessful()){
                                                               chatRequestReference.child(sendUserId).child(currentUsserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                   @Override
                                                                   public void onComplete(@NonNull Task<Void> task) {
                                                                      if(task.isSuccessful()){
                                                                          Toast.makeText(context, "Contact Saved", Toast.LENGTH_SHORT).show();
                                                                      }
                                                                   }
                                                               });
                                                           }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                    holder.btnRejectRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chatRequestReference.child(currentUsserId).child(sendUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                      chatRequestReference.child(sendUserId).child(currentUsserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                              if(task.isSuccessful()){
                                                  Toast.makeText(context, "Reject Request", Toast.LENGTH_SHORT).show();
                                              }
                                          }
                                      });
                                   }
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @NonNull
    @Override
    public ChatRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list,parent,false);
        return new ChatRequestViewHolder(v);
    }

    public class ChatRequestViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civProfile;
        TextView tvName, tvStatus;
        Button btnAcceptRequest, btnRejectRequest;

        public ChatRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            civProfile = itemView.findViewById(R.id.civProfile);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnAcceptRequest = itemView.findViewById(R.id.btnAcceptRequest);
            btnRejectRequest = itemView.findViewById(R.id.btnRejectRequest);
        }
    }
}
