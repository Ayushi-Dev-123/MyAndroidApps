package com.ayushi.apnachating.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushi.apnachating.R;
import com.ayushi.apnachating.model.Group;
import com.ayushi.apnachating.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddMemberIntoGroupAdapter extends FirebaseRecyclerAdapter<User, AddMemberIntoGroupAdapter.CreateGroupViewHolder> {

    String currentUserId;
    DatabaseReference memberReference;
    Group group;
    ArrayList<User> memberList;

    public AddMemberIntoGroupAdapter(@NonNull FirebaseRecyclerOptions<User> options, Group group) {
        super(options);
        this.group = group;
        memberList = new ArrayList<>();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        memberReference = FirebaseDatabase.getInstance().getReference("Groups").child(group.getGroupId()).child("member");
    }

    @Override
    protected void onBindViewHolder(@NonNull final CreateGroupViewHolder holder, int i, @NonNull final User user) {
        /*if(currentUserId.equals(user.getUid())){
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(0,0);
            holder.itemView.setLayoutParams(params);
            holder.itemView.setVisibility(View.GONE);
        }*/
        //else
        memberReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                    params.width = 0;
                    params.height = 0;
                    holder.itemView.setLayoutParams(params);
                }
                else {
                    Picasso.get().load(user.getImage()).placeholder(R.drawable.chat_logo2).into(holder.civProfile);
                    holder.tvStatus.setText(user.getStatus());
                    holder.tvName.setText(user.getName());

                    if(user.isChecked())
                        holder.civSelected.setVisibility(View.VISIBLE);
                    else
                        holder.civSelected.setVisibility(View.GONE);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user.setChecked(!user.isChecked());
                            if(user.isChecked()){
                                holder.civSelected.setVisibility(View.VISIBLE);
                                memberList.add(user);
                            }
                            else {
                                holder.civSelected.setVisibility(View.GONE);
                                memberList.remove(user);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public CreateGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_group_item_list,parent,false);
        return new CreateGroupViewHolder(v);
    }

    public class CreateGroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStatus;
        CircleImageView civProfile, civSelected;
        public CreateGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            civProfile = itemView.findViewById(R.id.civGroupIcon);
            civSelected = itemView.findViewById(R.id.civSelectContact);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
    public ArrayList<User> getSelectUserList(){
        return memberList;
    }
}
